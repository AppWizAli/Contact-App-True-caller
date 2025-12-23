package com.atul.truecallercontactapp

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivityContactDetailBinding
import com.atul.truecallercontactapp.databinding.DialogRecentMoreBinding

class ActivityContactDetail : AppCompatActivity() {
    private lateinit var binding: ActivityContactDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this@ActivityContactDetail, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

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
