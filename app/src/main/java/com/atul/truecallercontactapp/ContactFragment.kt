package com.atul.truecallercontactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactFragment : Fragment() {

    private lateinit var rvAllContacts: RecyclerView
    private lateinit var rvFavorites: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var headerPhone: RelativeLayout
    private lateinit var ivArrowDown: ImageView
    private lateinit var fabDial: ImageView // FAB Reference

    private lateinit var allContactsAdapter: AllContactsAdapter
    private var fullList: List<ContactModel> = emptyList()
    private var isExpanded = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact, container, false)

        rvAllContacts = view.findViewById(R.id.rvAllContacts)
        rvFavorites = view.findViewById(R.id.rvFavorites)
        etSearch = view.findViewById(R.id.searchText)
        headerPhone = view.findViewById(R.id.headerPhone)
        ivArrowDown = view.findViewById(R.id.ivArrowDown)
        fabDial = view.findViewById(R.id.fabDial) // Initialize FAB

        setupSearch()
        setupExpandCollapse()
        checkPermissionAndLoad()

        // FAB Dial Click Listener
        fabDial.setOnClickListener {
            openDialpadBottomSheet()
        }

        return view
    }

    private fun setupExpandCollapse() {
        headerPhone.setOnClickListener {
            if (isExpanded) {
                rvAllContacts.visibility = View.GONE
                ivArrowDown.animate().rotation(180f).setDuration(200).start()
            } else {
                rvAllContacts.visibility = View.VISIBLE
                ivArrowDown.animate().rotation(0f).setDuration(200).start()
            }
            isExpanded = !isExpanded
        }
    }

    private fun loadContacts() {
        viewLifecycleOwner.lifecycleScope.launch {
            val contacts = withContext(Dispatchers.IO) { fetchContactsFromSystem() }
            fullList = contacts

            allContactsAdapter = AllContactsAdapter(contacts)
            rvAllContacts.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = allContactsAdapter
            }

            val favs = contacts.filter { it.isStarred }
            rvFavorites.apply {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = FavoritesAdapter(favs)
            }
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }
            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun filterList(query: String) {
        val filtered = if (query.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.name.contains(query, ignoreCase = true) || it.phoneNumber.contains(query)
            }
        }
        if (::allContactsAdapter.isInitialized) {
            allContactsAdapter.updateList(filtered)
        }

        if (query.isNotEmpty() && !isExpanded) {
            headerPhone.performClick()
        }
    }

    // --- DIALPAD LOGIC ---

    private fun openDialpadBottomSheet() {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val sheetView = layoutInflater.inflate(R.layout.dialer_dialog, null)
        dialog.setContentView(sheetView)

        val tvNumber = sheetView.findViewById<TextView>(R.id.tvNumber)
        val btnBackspace = sheetView.findViewById<ImageButton>(R.id.btnBackspace)
        val btnCallSim1 = sheetView.findViewById<ImageButton>(R.id.btnCallsim1)
        val btnCallSim2 = sheetView.findViewById<ImageButton>(R.id.btnCallsim2)

        val btns = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
            R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9,
            R.id.btnstar, R.id.btnHash
        )

        btns.forEach { id ->
            sheetView.findViewById<TextView>(id)?.setOnClickListener {
                if (tvNumber.text.toString() == "..............") tvNumber.text = ""
                tvNumber.append((it as TextView).text)
            }
        }

        btnBackspace?.setOnClickListener {
            val text = tvNumber.text.toString()
            if (text.isNotEmpty() && text != "..............") tvNumber.text = text.dropLast(1)
        }

        val initiateCall = {
            val number = tvNumber.text.toString().replace(".", "")
            if (number.isNotEmpty()) {
                makePhoneCall(number)
                dialog.dismiss()
            }
        }

        btnCallSim1?.setOnClickListener { initiateCall() }
        btnCallSim2?.setOnClickListener { initiateCall() }

        dialog.show()
    }

    private fun makePhoneCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Call Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchContactsFromSystem(): List<ContactModel> {
        val list = mutableListOf<ContactModel>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.STARRED
        )

        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection, null, null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY} ASC"
        )

        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY)
            val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val starIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.STARRED)

            while (it.moveToNext()) {
                list.add(ContactModel("", it.getString(nameIdx) ?: "Unknown", it.getString(numIdx) ?: "", it.getInt(starIdx) == 1))
            }
        }
        return list
    }

    private fun checkPermissionAndLoad() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE), 101)
        }
    }
}