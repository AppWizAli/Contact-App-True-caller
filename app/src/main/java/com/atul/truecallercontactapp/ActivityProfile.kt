package com.atul.truecallercontactapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivityProfileBinding

class ActivityProfile : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSettings.setOnClickListener {
            intent= Intent(this@ActivityProfile, ActivitySetting::class.java)
            startActivity(intent)
            window.statusBarColor = ContextCompat.getColor(this@ActivityProfile, R.color.white_gray)

            // Light status bar (dark icons)
            WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        }

    }
}