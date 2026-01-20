package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentSettingsBinding
import com.amna.dailler.databinding.ItemSettingRowBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        
        setupRows()
    }

    private fun setupRows() {
        setupRow(binding.setAds.root, "Remove Ads", "Tap to get rid of ads")
        
        setupRow(binding.setAppearance.root, "Appearance", "Themes, text size, list item size, photos, etc.") {
            findNavController().navigate(R.id.nav_settings_appearance)
        }

        setupRow(binding.setBehavior.root, "Behavior", "Quick actions, search, call confirmation, missed call notifications, etc.")
        
        setupRow(binding.setDialpad.root, "Dialpad", "T9 settings, sound and vibro feedback, view, appearance, etc.") {
            findNavController().navigate(R.id.nav_settings_dialpad)
        }

        setupRow(binding.setContacts.root, "Contacts", "Contacts to display, sort order, contact card, name formats, etc.") {
            findNavController().navigate(R.id.nav_settings_contacts)
        }

        setupRow(binding.setHistory.root, "Call history", "Call grouping, sort order, recent call duration, etc.")
        setupRow(binding.setSim.root, "Dual SIM", "Settings for dual sim devices")
        setupRow(binding.setBlacklist.root, "Blacklist", "Select contacts and phone numbers to block")
        setupRow(binding.setLabs.root, "Labs", "Experimental features")
        setupRow(binding.setBackup.root, "Backup/Restore settings", "")
        setupRow(binding.setLanguage.root, "Interface language", "System")

        // About
        setupRow(binding.setThanks.root, "Say Thanks", "Enjoying the app? Please rate us and leave your comment on google play!")
        setupRow(binding.setShare.root, "Share App", "Share True Dialer with friends")
        setupRow(binding.setAboutApp.root, "About the App", "Short name/Info about the App")

        // Advanced
        setupRow(binding.setLicense.root, "License Agreement", "and Privacy Policy")

        // Debug
        setupRow(binding.setBug.root, "Report a bug", "Something wrong? help us to fix it")
        setupRow(binding.setReset.root, "Reset settings", "")
        setupRow(binding.setRestart.root, "Restart App", "")
    }

    private fun setupRow(root: View, title: String, desc: String, onClick: (() -> Unit)? = null) {
        val bindingRow = ItemSettingRowBinding.bind(root)
        bindingRow.tvTitle.text = title
        bindingRow.tvDesc.text = desc
        bindingRow.tvDesc.visibility = if (desc.isEmpty()) View.GONE else View.VISIBLE
        onClick?.let { cb ->
            root.setOnClickListener { cb() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
