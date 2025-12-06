package com.atul.truecallercontactapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PhoneFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_phone, container, false)

        // Example: Floating Dialpad Button
        val fabDialpad = view.findViewById<FloatingActionButton>(R.id.fabDial)
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
        val fabCall = sheetView.findViewById<FloatingActionButton>(R.id.fabCall)

        // Buttons 0-9, *, #
        val btns = listOf(
            sheetView.findViewById<Button>(R.id.btn0),
            sheetView.findViewById<Button>(R.id.btn1),
            sheetView.findViewById<Button>(R.id.btn2),
            sheetView.findViewById<Button>(R.id.btn3),
            sheetView.findViewById<Button>(R.id.btn4),
            sheetView.findViewById<Button>(R.id.btn5),
            sheetView.findViewById<Button>(R.id.btn6),
            sheetView.findViewById<Button>(R.id.btn7),
            sheetView.findViewById<Button>(R.id.btn8),
            sheetView.findViewById<Button>(R.id.btn9),
            sheetView.findViewById<Button>(R.id.btnStar),
            sheetView.findViewById<Button>(R.id.btnHash)
        )

        btns.forEach { btn ->
            btn.setOnClickListener {
                tvNumber.text = tvNumber.text.toString() + btn.text.toString()
            }
        }

        btnBackspace.setOnClickListener {
            val text = tvNumber.text.toString()
            if (text.isNotEmpty()) {
                tvNumber.text = text.substring(0, text.length - 1)
            }
        }

        fabCall.setOnClickListener {
            val number = tvNumber.text.toString()
            if (number.isNotEmpty()) {
                // TODO: Start call intent
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PhoneFragment()
    }
}
