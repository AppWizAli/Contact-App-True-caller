package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentCreateContactBinding

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CreateContactFragment : Fragment() {

    private var _binding: FragmentCreateContactBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()
    
    private var selectedAccount: AccountInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupAccountSelector()
        
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        
        binding.ivSave.setOnClickListener {
            saveContact()
        }
        
        binding.btnCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        
        binding.btnSave.setOnClickListener {
            saveContact()
        }

        arguments?.getString("contactNumber")?.let {
            binding.etPhone.setText(it)
        }
    }

    private fun setupAccountSelector() {
        binding.cardAccount.setOnClickListener {
            showAccountPicker()
        }
        
        // Default account
        lifecycleScope.launch {
            val accounts = viewModel.getAccounts()
            if (accounts.isNotEmpty()) {
                updateSelectedAccount(accounts.first())
            }
        }
    }

    private fun showAccountPicker() {
        lifecycleScope.launch {
            val accounts = viewModel.getAccounts()
            val popup = androidx.appcompat.widget.PopupMenu(requireContext(), binding.cardAccount)
            
            accounts.forEachIndexed { index, account ->
                popup.menu.add(0, index, 0, account.name)
            }
            
            popup.setOnMenuItemClickListener { item ->
                val account = accounts[item.itemId]
                updateSelectedAccount(account)
                true
            }
            popup.show()
        }
    }

    private fun updateSelectedAccount(account: AccountInfo) {
        selectedAccount = account
        binding.tvAccountName.text = account.name
    }

    private fun saveContact() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val phone = binding.etPhone.text.toString()

        if (firstName.isBlank() && lastName.isBlank()) {
            android.widget.Toast.makeText(requireContext(), "Please enter a name", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val success = viewModel.saveContact(firstName, lastName, phone, selectedAccount)
            if (success) {
                android.widget.Toast.makeText(requireContext(), "Contact saved", android.widget.Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                android.widget.Toast.makeText(requireContext(), "Failed to save contact", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
