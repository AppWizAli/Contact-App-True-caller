package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentContactsToDisplayBinding

class ContactsToDisplayFragment : Fragment() {

    private var _binding: FragmentContactsToDisplayBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsToDisplayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
        binding.btnOk.setOnClickListener { findNavController().popBackStack() }
        
        val accounts = listOf(
            AccountFilter("With Phones only", "Display contacts with phone numbers only", 0),
            AccountFilter("Phone", "Local contacts", 7),
            AccountFilter("Google", "alihassan@gmail.com", 325),
            AccountFilter("WhatsApp", "Messenger", 718),
            AccountFilter("SIM", "Local, Unsynced", 248)
        )
        binding.rvAccounts.adapter = AccountFilterAdapter(accounts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
