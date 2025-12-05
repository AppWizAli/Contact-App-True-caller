package com.atul.truecallercontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import android.widget.ImageView

class PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        // Find views
        val btnFilter = view.findViewById<ImageView>(R.id.iconFilter)
        val btnMore = view.findViewById<ImageView>(R.id.iconMore)

        // Set click listeners for popups
        btnFilter.setOnClickListener { showFilterPopup(it) }
        btnMore.setOnClickListener { showMoreOptionsPopup(it) }

        return view
    }

    // ==================== FILTER POPUP ====================
    private fun showFilterPopup(anchorView: View) {
        // Inflate popup layout
        val popupView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog_phone_simple_filter, null)

        // Create PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Transparent background
        popupWindow.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(
                android.graphics.Color.TRANSPARENT
            )
        )
        popupWindow.elevation = 8.0f
        popupWindow.isOutsideTouchable = true

        // Auto dismiss on outside touch
        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

        // Show popup below the icon
        popupWindow.showAsDropDown(anchorView, 0, 0)
    }

    // ==================== MORE OPTIONS POPUP ====================
    private fun showMoreOptionsPopup(anchorView: View) {
        // Inflate popup layout
        val popupView = LayoutInflater.from(requireContext())
            .inflate(R.layout.dialog__phone_more_option, null)

        // Create PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Transparent background
        popupWindow.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(
                android.graphics.Color.TRANSPARENT
            )
        )
        popupWindow.elevation = 8.0f
        popupWindow.isOutsideTouchable = true

        // Auto dismiss on outside touch
        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

        // Show popup below the icon
        popupWindow.showAsDropDown(anchorView, 0, 0)
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneFragment()
    }
}