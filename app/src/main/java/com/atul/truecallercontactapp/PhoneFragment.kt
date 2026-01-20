package com.atul.truecallercontactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class PhoneFragment : Fragment() {

    private lateinit var rvFavorites: RecyclerView
    private lateinit var rvRecents: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var tvSeeAllFavorites: TextView

    private var fullRecentList: List<CallLogModel> = emptyList()
    private lateinit var recentAdapter: RecentCallsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white_gray)
        WindowInsetsControllerCompat(requireActivity().window, view).isAppearanceLightStatusBars = true

        rvFavorites = view.findViewById(R.id.rvFavorites)
        rvRecents = view.findViewById(R.id.rvRecents)
        etSearch = view.findViewById(R.id.etSearchPhone)
        tvSeeAllFavorites = view.findViewById(R.id.fvrtSeeAll)

        loadData()
        setupSearch()

        val fabDialpad = view.findViewById<ImageView>(R.id.fabDial)
        fabDialpad.setOnClickListener {
            openDialpadBottomSheet()
        }

        tvSeeAllFavorites.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ContactFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterRecentCalls(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterRecentCalls(query: String) {
        val filteredList = if (query.isEmpty()) {
            fullRecentList
        } else {
            fullRecentList.filter {
                (it.name?.contains(query, ignoreCase = true) ?: false) ||
                        (it.number?.contains(query, ignoreCase = true) ?: false)
            }
        }

        if (::recentAdapter.isInitialized) {
            recentAdapter.updateList(filteredList)
        }
    }

    private fun loadData() {
        viewLifecycleOwner.lifecycleScope.launch {
            val favData = withContext(Dispatchers.IO) { fetchFavorites() }
            val recentData = withContext(Dispatchers.IO) { fetchCallLogs() }

            fullRecentList = recentData
            setupRecyclerViews(favData, recentData)
        }
    }

    private fun setupRecyclerViews(favData: List<ContactModel>, recentData: List<CallLogModel>) {
        rvFavorites.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvFavorites.adapter = FavoritesAdapter(favData)

        recentAdapter = RecentCallsAdapter(recentData)
        rvRecents.layoutManager = LinearLayoutManager(requireContext())
        rvRecents.adapter = recentAdapter
    }

    private fun fetchCallLogs(): List<CallLogModel> {
        val callList = mutableListOf<CallLogModel>()
        val cursor = requireContext().contentResolver.query(
            CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC"
        )

        cursor?.use {
            val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
            val numIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
            val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)
            val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
            val durationIdx = it.getColumnIndex(CallLog.Calls.DURATION)

            var count = 0
            while (it.moveToNext() && count < 50) {
                val callDate = it.getLong(dateIdx)
                val duration = it.getInt(durationIdx)
                val rawNumber = it.getString(numIdx) ?: ""
                val cachedName = it.getString(nameIdx)

                // Use the relative time string (e.g., "15 minutes ago")
                val relativeTime = android.text.format.DateUtils.getRelativeTimeSpanString(
                    callDate,
                    System.currentTimeMillis(),
                    android.text.format.DateUtils.MINUTE_IN_MILLIS
                ).toString()

                callList.add(CallLogModel(
                    name = if (cachedName.isNullOrEmpty()) rawNumber else cachedName,
                    number = rawNumber,
                    date = callDate.toString(),
                    duration = formatDuration(duration),
                    time = relativeTime,
                    callType = it.getInt(typeIdx),
                    label = "Mobile"
                ))
                count++
            }
        }
        return callList
    }

    private fun formatDuration(seconds: Int): String {
        return if (seconds == 0) "0s" else String.format("%d:%02d", seconds / 60, seconds % 60)
    }

    private fun fetchFavorites(): List<ContactModel> {
        val favList = mutableListOf<ContactModel>()
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )
        val cursor = requireContext().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            "${ContactsContract.Contacts.STARRED} = ?",
            arrayOf("1"),
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )
        cursor?.use {
            val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                favList.add(ContactModel("", it.getString(nameIdx) ?: "Unknown", it.getString(numIdx) ?: ""))
            }
        }
        return favList
    }

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

        val callAction = {
            val number = tvNumber.text.toString().replace(".", "")
            if (number.isNotEmpty()) {
                makePhoneCall(number)
                dialog.dismiss()
            }
        }

        btnCallSim1?.setOnClickListener { callAction() }
        btnCallSim2?.setOnClickListener { callAction() }
        dialog.show()
    }

    private fun makePhoneCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent)
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = PhoneFragment()
    }
}