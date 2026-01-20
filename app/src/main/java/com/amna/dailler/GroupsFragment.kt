package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import com.amna.dailler.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment() {

    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()
    
    private lateinit var groupsAdapter: ContactAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
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
        groupsAdapter = ContactAdapter { contact ->
            val bundle = Bundle().apply {
                putString("contactName", contact.name)
                putString("contactNumber", contact.number)
            }
            findNavController().navigate(R.id.nav_contact_detail, bundle)
        }
        binding.rvGroups.adapter = groupsAdapter
    }

    private fun setupListeners() {
        binding.ivMore.setOnClickListener {
            showOptionsMenu(it)
        }
    }

    private fun showOptionsMenu(view: View) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("Create group")
        popup.menu.add("Settings")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Create group" -> { /* Logic */ }
                "Settings" -> findNavController().navigate(R.id.nav_settings)
            }
            true
        }
        popup.show()
    }

    private fun setupObservations() {
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            groupsAdapter.submitList(contacts)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
