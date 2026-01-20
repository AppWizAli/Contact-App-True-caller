package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentSettingsDialpadBinding
import com.amna.dailler.databinding.ItemSettingRowBinding
import com.amna.dailler.databinding.ItemSettingSwitchBinding

class DialpadSettingsFragment : Fragment() {

    private var _binding: FragmentSettingsDialpadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsDialpadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        setupSettings()
    }

    private fun setupSettings() {
        setupRow(binding.setPrimaryT9.root, "Primary T9 language", "Default")
        setupRow(binding.setSecondaryT9.root, "Secondary T9 language", "Default") {
            showLanguageSelectionSheet()
        }
        setupRow(binding.setDisplayDefault.root, "Display by Default", "Always")
        setupRow(binding.setDialpadConfig.root, "Dialpad", "Dialpad and T9 text size, call button")
        
        setupSwitch(binding.swHideBack.root, "Hide by back-press", "Hide dialpad by back-press", true)
        setupSwitch(binding.swClearBack.root, "Clear by back-press", "Hide phone numbers by back-press")
        setupSwitch(binding.swClearOpen.root, "Clear when open", "Clear typed phone number when dialer is open", true)
        setupSwitch(binding.swFastDialLast.root, "Fast-dial last number", "Dial last known phone number by pressing call button")
        setupSwitch(binding.swFastDialSearch.root, "Fast-dial when search", "Dial the best result found when search by pressing call button")

        setupRow(binding.setActionButton.root, "Action button", "Add contact")
    }

    private fun showLanguageSelectionSheet() {
        val dialog = com.google.android.material.bottomsheet.BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_language_selection, null)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun setupRow(root: View, title: String, desc: String, onClick: (() -> Unit)? = null) {
        val bindingRow = ItemSettingRowBinding.bind(root)
        bindingRow.tvTitle.text = title
        bindingRow.tvDesc.text = desc
        root.setOnClickListener { onClick?.invoke() }
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
