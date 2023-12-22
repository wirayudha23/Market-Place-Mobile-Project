package com.example.ichoosecoffetoday.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ichoosecoffetoday.databinding.ActivityAkunTokoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AkunTokoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAkunTokoBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAkunTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi TextView untuk menampilkan username
        val usernameTokoTextView = binding.usernameToko

        // Ambil UID pengguna saat ini
        val uid = firebaseAuth.currentUser?.uid

        // Periksa jika UID tidak null
        if (uid != null) {
            // Akses Firestore untuk mendapatkan data pengguna
            val userDocument = firestore.collection("users").document(uid)

            userDocument.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        // Dokumen ditemukan, ambil nilai username
                        val username = document.getString("username")
                        // Set nilai username ke dalam TextView
                        usernameTokoTextView.text = username
                    } else {
                        // Dokumen tidak ditemukan
                        // Handle kasus ini sesuai kebutuhan Anda
                    }
                }
                .addOnFailureListener { e ->
                    // Gagal mengakses Firestore
                    // Handle kasus ini sesuai kebutuhan Anda
                }
        }

        binding.uploadProductButtone.setOnClickListener {
            val intent = Intent(this, UploadActivity::class.java)
            startActivity(intent)
        }

        binding.jual.setOnClickListener {
            val intent = Intent(this, UserProductsActivity::class.java)
            startActivity(intent)
        }
    }
}
