package com.amna.dailler

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentHistoryDetailBinding

class HistoryDetailFragment : Fragment() {

    private var _binding: FragmentHistoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        setupHistoryList()
    }

    private fun setupHistoryList() {
        val adapter = CallLogAdapter(
            onItemClick = { /* No action */ },
            onInfoClick = { /* No action */ },
            onItemLongClick = { /* No action */ }
        )
        binding.rvHistory.adapter = adapter
        
        val logs = listOf(
            CallLogEntry("1", null, "0345 7455800", "5:53 PM", CallType.INCOMING, "Pakistan"),
            CallLogEntry("2", null, "0324 7455870", "5:22 PM", CallType.OUTGOING, "Pakistan"),
            CallLogEntry("3", null, "0324 7455870", "5:19 PM", CallType.MISSED, "Pakistan")
        )
        adapter.submitList(logs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
