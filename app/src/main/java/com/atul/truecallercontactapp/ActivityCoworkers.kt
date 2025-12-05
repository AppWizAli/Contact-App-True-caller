package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atul.truecallercontactapp.databinding.ActivityCoworkersBinding

class ActivityCoworkers : AppCompatActivity() {
    private lateinit var binding: ActivityCoworkersBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityCoworkersBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}