package com.atul.truecallercontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog

class PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        // ✅ Set status bar color to white-gray with black icons
        requireActivity().window.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.white_gray)
        WindowInsetsControllerCompat(requireActivity().window, view).isAppearanceLightStatusBars = true

        // ✅ fabDial is ImageView (NOT FloatingActionButton)
        val fabDialpad = view.findViewById<ImageView>(R.id.fabDial)
        fabDialpad.setOnClickListener {
            openDialpadBottomSheet()
        }

        return view
    }

    private fun openDialpadBottomSheet() {

        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val sheetView = layoutInflater.inflate(R.layout.dialer_dialog, null)
        dialog.setContentView(sheetView)

        val tvNumber = sheetView.findViewById<TextView>(R.id.tvNumber)
        val btnBackspace = sheetView.findViewById<ImageButton>(R.id.btnBackspace)
        val btnCallMain = sheetView.findViewById<ImageView>(R.id.fabDial) // ⚡ Correct

        val btns = listOf(
            sheetView.findViewById<TextView>(R.id.btn0),
            sheetView.findViewById<TextView>(R.id.btn1),
            sheetView.findViewById<TextView>(R.id.btn2),
            sheetView.findViewById<TextView>(R.id.btn3),
            sheetView.findViewById<TextView>(R.id.btn4),
            sheetView.findViewById<TextView>(R.id.btn5),
            sheetView.findViewById<TextView>(R.id.btn6),
            sheetView.findViewById<TextView>(R.id.btn7),
            sheetView.findViewById<TextView>(R.id.btn8),
            sheetView.findViewById<TextView>(R.id.btn9),
            sheetView.findViewById<TextView>(R.id.btnstar),
            sheetView.findViewById<TextView>(R.id.btnHash)
        )

        btns.forEach { btn ->
            btn.setOnClickListener {
                tvNumber.text = tvNumber.text.toString() + btn.text.toString()
            }
        }

        btnBackspace.setOnClickListener {
            val text = tvNumber.text.toString()
            if (text.isNotEmpty()) {
                tvNumber.text = text.dropLast(1)
            }
        }

        btnCallMain.setOnClickListener {
            val number = tvNumber.text.toString()
            if (number.isNotEmpty()) {
                // TODO: implement call intent
                dialog.dismiss()
            }
        }

        dialog.show()
    }


    companion object {
        fun newInstance() = PhoneFragment()
    }
}
