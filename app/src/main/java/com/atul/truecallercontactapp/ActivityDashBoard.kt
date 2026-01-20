package com.atul.truecallercontactapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.atul.truecallercontactapp.databinding.ActivityDashBoardBinding

class ActivityDashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityDashBoardBinding

    // List of permissions required for the home page (Pic 2)
    private val REQUEST_CODE_PERMISSIONS = 123
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // UI Styling
        window.statusBarColor = ContextCompat.getColor(this, R.color.white_gray)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        // Check permissions before loading data
        if (allPermissionsGranted()) {
            loadFragment(PhoneFragment())
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_Contact -> loadFragment(ContactFragment())
                R.id.nav_Phone -> loadFragment(PhoneFragment())
                R.id.nav_Group -> loadFragment(GroupFragment())
                else -> false
            }
            true
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                loadFragment(PhoneFragment())
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
        return true
    }
}