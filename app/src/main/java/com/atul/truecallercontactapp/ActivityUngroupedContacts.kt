package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.atul.truecallercontactapp.databinding.ActivityUngroupedContactsBinding

class ActivityUngroupedContacts : AppCompatActivity() {
    private lateinit var binding: ActivityUngroupedContactsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityUngroupedContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}