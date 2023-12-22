package com.example.ichoosecoffetoday.login_regis

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.signupButton.setOnClickListener {
            val email = binding.signupEmail.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirm.text.toString()
            val username = binding.signupUsername.text.toString()  // Ambil nilai dari EditText username
            val alamat = binding.signupAlamat.text.toString()  // Ambil nilai dari EditText alamat

            if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty() && alamat.isNotEmpty()) {
                if (password == confirmPassword) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                val uid = user?.uid

                                val firestore = FirebaseFirestore.getInstance()
                                val userDocument = firestore.collection("users").document(uid.toString())

                                val userData = hashMapOf(
                                    "username" to username,
                                    "alamat" to alamat
                                    // tambahkan data lainnya sesuai kebutuhan
                                )

                                userDocument.set(userData)
                                    .addOnSuccessListener {
                                        // Data pengguna berhasil disimpan
                                        // Anda dapat menavigasi ke layar selanjutnya atau melakukan tindakan lainnya
                                    }
                                    .addOnFailureListener { e ->
                                        // Gagal menyimpan data pengguna
                                        Toast.makeText(this, "Gagal menyimpan data pengguna: $e", Toast.LENGTH_SHORT).show()
                                    }

                            } else {
                                Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Kata sandi tidak cocok", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirectText.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }
}
