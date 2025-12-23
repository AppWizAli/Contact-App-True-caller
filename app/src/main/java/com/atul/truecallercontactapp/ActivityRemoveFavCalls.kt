package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivityRemoveFavCallsBinding

class ActivityRemoveFavCalls : AppCompatActivity() {
    private lateinit var binding: ActivityRemoveFavCallsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemoveFavCallsBinding.inflate(layoutInflater)
        // Status bar color set
        window.statusBarColor = ContextCompat.getColor(this@ActivityRemoveFavCalls, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        setContentView(binding.root)
      }
    }
