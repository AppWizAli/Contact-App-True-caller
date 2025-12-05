package com.atul.truecallercontactapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.atul.truecallercontactapp.databinding.ActivityDashBoardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ActivityDashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = 0
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        // Default fragment
        loadFragment(PhoneFragment())

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.nav_Contact -> {
                    loadFragment(ContactFragment())
                    true
                }

                R.id.nav_Phone -> {
                    loadFragment(PhoneFragment())
                    true
                }
                R.id.nav_Group -> {
                    loadFragment(GroupFragment())
                    true
                }

                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}