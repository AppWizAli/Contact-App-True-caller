package com.atul.truecallercontactapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.atul.truecallercontactapp.databinding.ActivitySettingBinding

class ActivitySetting : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.statusBarColor = ContextCompat.getColor(this@ActivitySetting, R.color.white_gray)

        // Light status bar (dark icons)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding.btnAppearance.setOnClickListener {
            intent= Intent(this@ActivitySetting, ActivityAppearance::class.java)
            startActivity(intent)

    }
        binding.btnDialpad.setOnClickListener {
            intent= Intent(this@ActivitySetting, ActivityDialPad::class.java)
            startActivity(intent)

        }
        binding.btnContacts.setOnClickListener {
            intent= Intent(this@ActivitySetting, ActivityContactSet::class.java)
            startActivity(intent)

        }

}
}