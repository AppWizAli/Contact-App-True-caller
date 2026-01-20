package com.amna.dailler

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DialerState(
    val contacts: List<Contact> = emptyList(),
    val callLogs: List<CallLogEntry> = emptyList(),
    val favorites: List<Contact> = emptyList(),
    val filteredContacts: List<Contact> = emptyList(),
    val filteredLogs: List<CallLogEntry> = emptyList()
)

class DialerViewModel(application: Application) : AndroidViewModel(application) {

    private val contactsRepository = ContactsRepository(application)
    private val callLogRepository = CallLogRepository(application)
    private var isRefreshing = false
    private var lastRefreshTime = 0L
    private val REFRESH_DEBOUNCE_MS = 1000L // Prevent multiple refreshes within 1 second

    private val _state = MutableLiveData<DialerState>()
    val state: LiveData<DialerState> = _state

    // Compatibility properties for current fragment observers
    private val _contacts = MutableLiveData<List<Contact>>()
    val contacts: LiveData<List<Contact>> = _contacts

    private val _callLogs = MutableLiveData<List<CallLogEntry>>()
    val callLogs: LiveData<List<CallLogEntry>> = _callLogs

    private val _favorites = MutableLiveData<List<Contact>>()
    val favorites: LiveData<List<Contact>> = _favorites

    private val _searchQuery = MutableLiveData<String>("")
    val searchQuery: LiveData<String> = _searchQuery
    
    private val _callLogFilter = MutableLiveData<CallLogFilter>(CallLogFilter.ALL)
    val callLogFilter: LiveData<CallLogFilter> = _callLogFilter

    private var allContacts = emptyList<Contact>()
    private var allLogs = emptyList<CallLogEntry>()

    private var searchJob: Job? = null
    
    fun setSearchQuery(query: String) {
        // Cancel previous search job
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            // Debounce: wait 300ms before searching
            delay(300)
            _searchQuery.value = query
            filterData()
        }
    }
    
    fun setCallLogFilter(filter: CallLogFilter) {
        _callLogFilter.value = filter
        filterData()
    }

    fun refreshData() {
        // Debounce: prevent multiple rapid refreshes
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime < REFRESH_DEBOUNCE_MS) {
            return
        }
        lastRefreshTime = currentTime
        
        if (isRefreshing) return
        isRefreshing = true
        
        viewModelScope.launch {
            try {
                allContacts = contactsRepository.getContacts()
                allLogs = callLogRepository.getCallLogs()
                filterData()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isRefreshing = false
            }
        }
    }

    private fun filterData() {
        viewModelScope.launch {
            val query = _searchQuery.value ?: ""
            val filter = _callLogFilter.value ?: CallLogFilter.ALL
            
            val (filteredContacts, filteredLogs, favorites) = withContext(Dispatchers.Default) {
                val fc = if (query.isEmpty()) {
                    allContacts
                } else {
                    allContacts.filter { 
                        it.name.contains(query, ignoreCase = true) || 
                        it.number.contains(query, ignoreCase = true)
                    }
                }

                val fl = if (query.isEmpty()) {
                    when (filter) {
                        CallLogFilter.ALL -> allLogs
                        CallLogFilter.INCOMING -> allLogs.filter { it.type == CallType.INCOMING }
                        CallLogFilter.OUTGOING -> allLogs.filter { it.type == CallType.OUTGOING }
                        CallLogFilter.MISSED -> allLogs.filter { it.type == CallType.MISSED }
                        CallLogFilter.BLOCKED -> allLogs.filter { it.type == CallType.BLOCKED }
                    }
                } else {
                    val queryFiltered = allLogs.filter { 
                        it.name?.contains(query, ignoreCase = true) == true || 
                        it.number.contains(query, ignoreCase = true)
                    }
                    when (filter) {
                        CallLogFilter.ALL -> queryFiltered
                        CallLogFilter.INCOMING -> queryFiltered.filter { it.type == CallType.INCOMING }
                        CallLogFilter.OUTGOING -> queryFiltered.filter { it.type == CallType.OUTGOING }
                        CallLogFilter.MISSED -> queryFiltered.filter { it.type == CallType.MISSED }
                        CallLogFilter.BLOCKED -> queryFiltered.filter { it.type == CallType.BLOCKED }
                    }
                }

                val favs = allContacts.filter { it.isFavorite }
                Triple(fc, fl, favs)
            }

            _contacts.value = filteredContacts
            _callLogs.value = filteredLogs
            _favorites.value = favorites
            _state.value = DialerState(allContacts, allLogs, favorites, filteredContacts, filteredLogs)
        }
    }
    suspend fun getAccounts(): List<AccountInfo> {
        return contactsRepository.getAccounts()
    }

    suspend fun toggleFavorite(contactId: String, currentStatus: Boolean): Boolean {
        val success = contactsRepository.setContactFavorite(contactId, !currentStatus)
        if (success) {
            refreshData()
        }
        return success
    }

    suspend fun saveContact(
        firstName: String,
        lastName: String,
        phone: String,
        account: AccountInfo?
    ): Boolean {
        val success = contactsRepository.saveContact(
            firstName,
            lastName,
            phone,
            account?.name,
            account?.type
        )
        if (success) {
            refreshData()
        }
        return success
    }
    
    suspend fun deleteContact(contactId: String): Boolean {
        val success = contactsRepository.deleteContact(contactId)
        if (success) {
            refreshData()
        }
        return success
    }
    
    suspend fun getContactVCard(contactId: String): String? {
        return contactsRepository.getContactVCard(contactId)
    }
}
