package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivityContactDetailBinding
import com.atul.truecallercontactapp.databinding.ActivityDeleteContactBinding

class ActivityDeleteContact : AppCompatActivity() {
    private lateinit var binding: ActivityDeleteContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityDeleteContactBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this@ActivityDeleteContact, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }
}