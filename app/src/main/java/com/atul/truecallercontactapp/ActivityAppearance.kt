package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atul.truecallercontactapp.databinding.ActivityAppearanceBinding

class ActivityAppearance : AppCompatActivity() {
    private lateinit var binding: ActivityAppearanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityAppearanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}