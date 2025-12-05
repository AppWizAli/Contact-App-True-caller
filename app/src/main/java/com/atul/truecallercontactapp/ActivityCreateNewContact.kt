package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atul.truecallercontactapp.databinding.ActivityCreateNewContactBinding

class ActivityCreateNewContact : AppCompatActivity() {
    private lateinit var binding: ActivityCreateNewContactBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNewContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}