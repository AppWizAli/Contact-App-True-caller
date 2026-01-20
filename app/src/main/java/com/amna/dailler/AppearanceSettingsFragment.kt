package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentSettingsAppearanceBinding
import com.amna.dailler.databinding.ItemSettingRowBinding

class AppearanceSettingsFragment : Fragment() {

    private var _binding: FragmentSettingsAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsAppearanceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        setupRows()
    }

    private fun setupRows() {
        setupRow(binding.setTheme.root, "Theme", "Dark Blue")
        setupRow(binding.setTabs.root, "Tabs", "Edit visible tabs and order")
        setupRow(binding.setTextSize.root, "Text size", "100%")
        setupRow(binding.setDialpadStyle.root, "Dialpad", "Dialpad appearance")
        setupRow(binding.setListSize.root, "List item size", "Height: 100%, photo size: 100%")
        setupRow(binding.setFavLayout.root, "Favorite contacts layout", "Grid, 3 columns")
        
        setupRow(binding.setCustomPhoto.root, "Photos and Avatar", "Customized photos style and avatars")
        setupRow(binding.setIncomingStyle.root, "Incoming call screen", "Photo style, answer method, visuals")
        setupRow(binding.setOngoingStyle.root, "Ongoing call screen", "Photo style, visuals")
    }

    private fun setupRow(root: View, title: String, desc: String) {
        val bindingRow = ItemSettingRowBinding.bind(root)
        bindingRow.tvTitle.text = title
        bindingRow.tvDesc.text = desc
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
