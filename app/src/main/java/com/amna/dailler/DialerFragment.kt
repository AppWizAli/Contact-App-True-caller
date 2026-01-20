package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentDialerBinding

class DialerFragment : Fragment() {

    private var _binding: FragmentDialerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()
    
    private lateinit var favoritesAdapter: FavoriteAdapter
    private lateinit var callLogAdapter: CallLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapters()
        setupObservations()
        setupListeners()
        setupSearch()
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun setupAdapters() {
        favoritesAdapter = FavoriteAdapter { contact ->
            val bundle = Bundle().apply {
                putString("contactName", contact.name)
                putString("contactNumber", contact.number)
                putString("contactId", contact.id)
            }
            findNavController().navigate(R.id.nav_contact_detail, bundle)
        }
        binding.rvFavorites.adapter = favoritesAdapter

        callLogAdapter = CallLogAdapter(
            onItemClick = { entry ->
                val bundle = Bundle().apply {
                    putString("contactName", entry.name ?: entry.number)
                    putString("contactNumber", entry.number)
                }
                findNavController().navigate(R.id.nav_contact_detail, bundle)
            },
            onInfoClick = { entry ->
                val bundle = Bundle().apply {
                    putString("contactName", entry.name ?: entry.number)
                    putString("contactNumber", entry.number)
                }
                findNavController().navigate(R.id.nav_history_detail, bundle)
            },
            onItemLongClick = { showCallLogOptionsSheet(it) }
        )
        binding.rvCallLogs.adapter = callLogAdapter
    }

    private fun setupObservations() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
            binding.rvFavorites.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.callLogs.observe(viewLifecycleOwner) { logs ->
            callLogAdapter.submitList(logs)
        }
    }

    private fun showCallLogOptionsSheet(entry: CallLogEntry) {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val sheetBinding = com.amna.dailler.databinding.BottomSheetCallOptionsBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        
        sheetBinding.optCall.tvText.text = "Call ${entry.number}"
        sheetBinding.optMessage.tvText.text = "Message ${entry.number}"
        sheetBinding.optHistory.tvText.text = "View history"
        
        sheetBinding.optCall.root.setOnClickListener {
            // Initiate call
            dialog.dismiss()
        }
        sheetBinding.optMessage.root.setOnClickListener {
            // Open message
            dialog.dismiss()
        }
        sheetBinding.optHistory.root.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contactName", entry.name ?: entry.number)
                putString("contactNumber", entry.number)
            }
            findNavController().navigate(R.id.nav_contact_detail, bundle)
            dialog.dismiss()
        }
        
        dialog.show()
    }

    private fun setupListeners() {
        binding.ivAvatar.setOnClickListener {
            findNavController().navigate(R.id.nav_account)
        }

        binding.fabDialpad.setOnClickListener {
            findNavController().navigate(R.id.nav_dialpad)
        }

        binding.ivFilter.setOnClickListener {
            showFilterSheet()
        }

        binding.ivMore.setOnClickListener {
            showOptionsMenu(it)
        }
        
        binding.tvSeeAll.setOnClickListener {
            findNavController().navigate(R.id.nav_contacts)
        }
    }

    private fun showOptionsMenu(view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("Clear call history")
        popup.menu.add("Settings")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Clear call history" -> showClearHistoryDialog()
                "Settings" -> findNavController().navigate(R.id.nav_settings)
            }
            true
        }
        popup.show()
    }

    private fun showFilterSheet() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val sheetBinding = com.amna.dailler.databinding.BottomSheetFiltersBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        
        sheetBinding.rgFilters.setOnCheckedChangeListener { _, checkedId ->
            val filter = when (checkedId) {
                R.id.rb_all -> CallLogFilter.ALL
                R.id.rb_incoming -> CallLogFilter.INCOMING
                R.id.rb_outgoing -> CallLogFilter.OUTGOING
                R.id.rb_missed -> CallLogFilter.MISSED
                R.id.rb_blocked -> CallLogFilter.BLOCKED
                else -> CallLogFilter.ALL
            }
            viewModel.setCallLogFilter(filter)
            dialog.dismiss()
        }
        
        dialog.show()
    }

    private fun showClearHistoryDialog() {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Clear call history")
            .setMessage("This action cannot be undone. Are you sure you want to continue?")
            .setNegativeButton("No") { d, _ -> d.dismiss() }
            .setPositiveButton("Yes") { _, _ -> /* Clear History */ }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
