package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentSettingsContactsBinding
import com.amna.dailler.databinding.ItemSettingRowBinding
import com.amna.dailler.databinding.ItemSettingSwitchBinding

class ContactsSettingsFragment : Fragment() {

    private var _binding: FragmentSettingsContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        setupSettings()
    }

    private fun setupSettings() {
        setupRow(binding.setDisplay.root, "Contacts to display", "Show/Hide specific accounts or groups from your contacts list")
        setupRow(binding.setNameView.root, "View contact names as", "Give name first")
        setupRow(binding.setSort.root, "Sort Contacts by", "Given name")
        
        setupSwitch(binding.swFormatNumbers.root, "Format phone numbers", "Enable phone numbers formatting", true)
        setupSwitch(binding.swLongNames.root, "Long contact names", "Allow 2-lines display names")
        
        setupRow(binding.setAdditionalInfo.root, "Additional contact info", "Choose additional contact info to display in contacts list, such as nickname, etc.")
        setupRow(binding.setNewContactDialog.root, "New contact dialog", "Select input Fields to show when creating new contact and editing contact name")
        setupRow(binding.setContactCard.root, "Contact card", "Additional contact card settings")

        setupSwitch(binding.swShowAlphabet.root, "Show alphabet", "Dial last known phone number by pressing call button", true)
        setupSwitch(binding.swAlphabetSeparators.root, "Show alphabetically separators", "Separate contacts by alphabet")
        setupSwitch(binding.swAutoKeyboard.root, "Auto show keyboard", "Automatically open keyboard when switching to contacts")
    }

    private fun setupRow(root: View, title: String, desc: String) {
        val bindingRow = ItemSettingRowBinding.bind(root)
        bindingRow.tvTitle.text = title
        bindingRow.tvDesc.text = desc
    }

    private fun setupSwitch(root: View, title: String, desc: String, isChecked: Boolean = false) {
        val bindingRow = ItemSettingSwitchBinding.bind(root)
        bindingRow.tvTitle.text = title
        bindingRow.tvDesc.text = desc
        bindingRow.swToggle.isChecked = isChecked
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
