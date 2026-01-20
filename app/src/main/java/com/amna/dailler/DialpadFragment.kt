package com.amna.dailler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amna.dailler.databinding.FragmentDialpadBinding

class DialpadFragment : Fragment() {

    private var _binding: FragmentDialpadBinding? = null
    private val binding get() = _binding!!
    private var currentNumber = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialpadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupKeypad()
        setupActions()
    }

    private fun setupKeypad() {
        val keys = listOf(
            binding.btn1, binding.btn2, binding.btn3,
            binding.btn4, binding.btn5, binding.btn6,
            binding.btn7, binding.btn8, binding.btn9,
            binding.btnStar, binding.btn0, binding.btnHash
        )
        val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "*", "0", "#")
        val letters = listOf("", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ", "", "+", "")

        keys.forEachIndexed { index, itemBinding ->
            val digit = digits[index]
            val letter = letters[index]
            
            itemBinding.tvDigit.text = digit
            itemBinding.tvLetters.text = letter
            
            itemBinding.root.setOnClickListener { appendNumber(digit) }
            
            if (digit == "0") {
                itemBinding.root.setOnLongClickListener {
                    appendNumber("+")
                    true
                }
            }
        }
    }

    private fun setupActions() {
        binding.ivBackspace.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                updateDisplay()
            }
        }

        binding.ivBackspace.setOnLongClickListener {
            currentNumber = ""
            updateDisplay()
            true
        }

        binding.btnCall.setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                initiateCall(currentNumber)
            }
        }
        
        binding.btnHideKeyboard.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCreateContact.setOnClickListener {
            val bundle = Bundle().apply {
                putString("contactNumber", currentNumber)
            }
            findNavController().navigate(R.id.nav_create_contact, bundle)
        }
    }

    private fun appendNumber(num: String) {
        currentNumber += num
        updateDisplay()
    }

    private fun updateDisplay() {
        binding.tvEnteredNumber.text = currentNumber
        binding.ivBackspace.visibility = if (currentNumber.isNotEmpty()) View.VISIBLE else View.GONE
        binding.btnCreateContact.visibility = if (currentNumber.isNotEmpty()) View.VISIBLE else View.GONE
    }

    private fun initiateCall(number: String) {
        // Show SIM Selection if dual SIM, or just call
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${Uri.encode(number)}"))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
