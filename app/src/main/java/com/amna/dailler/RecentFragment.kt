package com.amna.dailler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.amna.dailler.databinding.FragmentRecentBinding

class RecentFragment : Fragment() {

    private var _binding: FragmentRecentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()
    private lateinit var callLogAdapter: CallLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupObservations()
    }

    private fun setupAdapter() {
        callLogAdapter = CallLogAdapter(
            onItemClick = { makeCall(it.number) },
            onInfoClick = { /* Show options if needed */ },
            onItemLongClick = { /* Show options if needed */ }
        )
        binding.rvRecent.layoutManager = LinearLayoutManager(context)
        binding.rvRecent.adapter = callLogAdapter
    }

    private fun setupObservations() {
        viewModel.callLogs.observe(viewLifecycleOwner) { logs ->
            callLogAdapter.submitList(logs)
        }
    }

    private fun makeCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$number")
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

