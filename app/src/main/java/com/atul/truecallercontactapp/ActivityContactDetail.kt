package com.atul.truecallercontactapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.atul.truecallercontactapp.databinding.ActivityContactDetailBinding
import com.atul.truecallercontactapp.databinding.DialogRecentMoreBinding

class ActivityContactDetail : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // More button click
        binding.btnMore.setOnClickListener {
            showMoreOptionsDialog()
        }
    }

    private fun showMoreOptionsDialog() {
        // Correct binding inflate
        val dialogBinding = DialogRecentMoreBinding.inflate(layoutInflater)
        val dialog = Dialog(this)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        // Close dialog when any item is clicked
        dialogBinding.tvViewCallHistory.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvBlock.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvShare.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvRemoveFavourite.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvMergeSplit.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvChangeAccount.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvRemoveAccount.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvDelete.setOnClickListener { dialog.dismiss() }
        dialogBinding.tvSetRingtone.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }
}
