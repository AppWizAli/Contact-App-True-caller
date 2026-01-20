package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()
    
    private lateinit var favoritesAdapter: FavoriteAdapter
    private lateinit var frequentAdapter: ContactAdapter
    private lateinit var emergencyAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
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

        frequentAdapter = ContactAdapter { contact ->
            val bundle = Bundle().apply {
                putString("contactName", contact.name)
                putString("contactNumber", contact.number)
                putString("contactId", contact.id)
            }
            findNavController().navigate(R.id.nav_contact_detail, bundle)
        }
        binding.rvFrequent.adapter = frequentAdapter

        emergencyAdapter = ContactAdapter { contact ->
            val bundle = Bundle().apply {
                putString("contactName", contact.name)
                putString("contactNumber", contact.number)
                putString("contactId", contact.id)
            }
            findNavController().navigate(R.id.nav_contact_detail, bundle)
        }
        binding.rvEmergency.adapter = emergencyAdapter
    }

    private fun setupListeners() {
        binding.fabAddContact.setOnClickListener {
            findNavController().navigate(R.id.nav_create_contact)
        }
        
        binding.ivMore.setOnClickListener {
            showOptionsMenu(it)
        }
    }

    private fun showOptionsMenu(view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("Delete contacts")
        popup.menu.add("Import/Export")
        popup.menu.add("Settings")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Delete contacts" -> findNavController().navigate(R.id.nav_delete_contacts)
                "Import/Export" -> showImportExportSheet()
                "Settings" -> findNavController().navigate(R.id.nav_settings)
            }
            true
        }
        popup.show()
    }

    private fun showImportExportSheet() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        // Simple demo sheet
        val view = layoutInflater.inflate(R.layout.bottom_sheet_call_options, null)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupObservations() {
        viewModel.favorites.observe(viewLifecycleOwner) { favorites ->
            favoritesAdapter.submitList(favorites)
        }

        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            frequentAdapter.submitList(contacts)
            emergencyAdapter.submitList(contacts.filter { it.number == "1122" || it.number == "15" })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
