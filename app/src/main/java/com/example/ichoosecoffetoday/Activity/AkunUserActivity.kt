package com.example.ichoosecoffetoday.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.databinding.ActivityAkunUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AkunUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAkunUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Ambil data pengguna dari SharedPreferences
        val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        // Setel username ke tampilan
        binding.usernameProfil.text = username

        binding.akunTokoButton.setOnClickListener {
            val intent = Intent(this, AkunTokoActivity::class.java)
            startActivity(intent)
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle click on Home menu item
                    // Memulai ulang MainActivity jika belum berada di MainActivity
                    val intent = Intent(this@AkunUserActivity, MainActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.profile -> {
                    // Handle click on Profile menu item
                    // Start UploadActivity
                    val intent = Intent(this@AkunUserActivity, AkunUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                // ... (tambahkan penanganan item lain jika diperlukan)
                else -> false
            }
        }
    }
}
