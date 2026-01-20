package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentAccountBinding
import com.amna.dailler.databinding.ItemAccountOptionBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        
        setupOptions()
    }

    private fun setupOptions() {
        val settingsOpt = ItemAccountOptionBinding.bind(binding.optSettings.root)
        settingsOpt.tvText.text = "Settings"
        settingsOpt.ivIcon.setImageResource(R.drawable.ic_more_vert_24)
        settingsOpt.root.setOnClickListener {
            findNavController().navigate(R.id.nav_settings)
        }

        val helpOpt = ItemAccountOptionBinding.bind(binding.optHelp.root)
        helpOpt.tvText.text = "Help"
        helpOpt.ivIcon.setImageResource(R.drawable.ic_search_24)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
