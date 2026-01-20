package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentDeleteContactsBinding

class DeleteContactsFragment : Fragment() {

    private var _binding: FragmentDeleteContactsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeleteContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        setupDeleteList()
    }

    private fun setupDeleteList() {
        val contacts = listOf(
            Contact("1", "Ali jan Baloch", "0345 7455800"),
            Contact("2", "Danial dai", "0345 7455800"),
            Contact("3", "Billa bhai", "0345 7455800")
        )
        // Would use a SelectionAdapter here
        binding.tvDeleteBtn.text = "Delete (${contacts.size})"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
