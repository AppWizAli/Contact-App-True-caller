package com.amna.dailler

import android.content.Context
import android.provider.CallLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CallLogRepository(private val context: Context) {

    suspend fun getCallLogs(): List<CallLogEntry> = withContext(Dispatchers.IO) {
        val logs = mutableListOf<CallLogEntry>()
        val projection = arrayOf(
            CallLog.Calls._ID,
            CallLog.Calls.CACHED_NAME,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DATE,
            CallLog.Calls.TYPE,
            CallLog.Calls.COUNTRY_ISO
        )

        val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
        
        try {
            context.contentResolver.query(CallLog.Calls.CONTENT_URI, projection, null, null, "${CallLog.Calls.DATE} DESC")?.use { cursor ->
                val idIndex = cursor.getColumnIndex(CallLog.Calls._ID)
                val nameIndex = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)
                val numberIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER)
                val dateIndex = cursor.getColumnIndex(CallLog.Calls.DATE)
                val typeIndex = cursor.getColumnIndex(CallLog.Calls.TYPE)
                val countryIndex = cursor.getColumnIndex(CallLog.Calls.COUNTRY_ISO)

                while (cursor.moveToNext() && logs.size < 500) {
                    val id = cursor.getString(idIndex)
                    val name = cursor.getString(nameIndex) ?: "Unknown"
                    val number = cursor.getString(numberIndex) ?: "Unknown"
                    val date = cursor.getLong(dateIndex)
                    val typeInt = cursor.getInt(typeIndex)
                    val country = cursor.getString(countryIndex)

                    val callType = when (typeInt) {
                        CallLog.Calls.INCOMING_TYPE -> CallType.INCOMING
                        CallLog.Calls.OUTGOING_TYPE -> CallType.OUTGOING
                        CallLog.Calls.MISSED_TYPE -> CallType.MISSED
                        else -> CallType.INCOMING
                    }

                    val time = sdf.format(Date(date))
                    logs.add(CallLogEntry(id, name, number, time, callType, country))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext logs
    }
}
