package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivityFavouriteCallsBinding

class ActivityFavouriteCalls : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteCallsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouriteCallsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Status bar color set
        window.statusBarColor = ContextCompat.getColor(this@ActivityFavouriteCalls, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
    }
}
