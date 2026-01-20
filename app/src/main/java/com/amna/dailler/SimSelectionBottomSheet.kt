package com.amna.dailler

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.amna.dailler.databinding.BottomSheetSimBinding
import com.amna.dailler.databinding.ItemSimCardBinding
import android.widget.Toast

class SimSelectionBottomSheet(
    private val numberToCall: String,
    private val onSimSelected: (Int) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetSimBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSimBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCallingMessage.text = "Calling $numberToCall"
        
        binding.btnClose.setOnClickListener {
            dismiss()
        }

        loadSimCards()
    }

    @SuppressLint("MissingPermission")
    private fun loadSimCards() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
             Toast.makeText(context, "Permission needed to read SIMs", Toast.LENGTH_SHORT).show()
             // Fallback to dummies if permission missing, or show error
             addSimItem("SIM 1", "Carrier A", 0)
             addSimItem("SIM 2", "Carrier B", 1)
             return
        }

        val subscriptionManager = requireContext().getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
        val activeSubscriptionInfoList: List<SubscriptionInfo>? = subscriptionManager.activeSubscriptionInfoList

        if (activeSubscriptionInfoList != null && activeSubscriptionInfoList.isNotEmpty()) {
            for (subscriptionInfo in activeSubscriptionInfoList) {
                val simName = subscriptionInfo.displayName.toString()
                val carrierName = subscriptionInfo.carrierName.toString()
                val subId = subscriptionInfo.subscriptionId // or subscriptionInfo.simSlotIndex for older APIs
                val slotIndex = subscriptionInfo.simSlotIndex
                
                // You can also get number if permission READ_PHONE_NUMBERS is granted and available
                // val number = subscriptionInfo.number 

                addSimItem(simName, carrierName, slotIndex)
            }
        } else {
             // No SIMs found or error
             Toast.makeText(context, "No SIM cards found", Toast.LENGTH_SHORT).show()
             // Add dummy for testing UI
             addSimItem("SIM 1 (Debug)", "No SIM Found", 0)
        }
    }

    private fun addSimItem(name: String, carrier: String, slotIndex: Int) {
        val itemBinding = ItemSimCardBinding.inflate(layoutInflater, binding.simListContainer, false)
        itemBinding.tvSimName.text = name
        itemBinding.tvCarrier.text = carrier
        itemBinding.root.setOnClickListener {
            // Update UI to show selection (radio button)
            // For now, just select directly
            itemBinding.rbSim.isChecked = true
            onSimSelected(slotIndex)
            dismiss()
        }
        binding.simListContainer.addView(itemBinding.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
