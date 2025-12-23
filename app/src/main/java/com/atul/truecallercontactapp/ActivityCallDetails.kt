package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


class ActivityCallDetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_delete_contact)
        window.statusBarColor = ContextCompat.getColor(this@ActivityCallDetails, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

    }
}