package com.example.ichoosecoffetoday.Activity

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.ichoosecoffetoday.Adapter.ProductAdapter
import com.example.ichoosecoffetoday.Domain.Product
import com.example.ichoosecoffetoday.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Intent
import android.util.Log
import android.widget.TextView
import com.example.ichoosecoffetoday.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataList: ArrayList<Product>
    private lateinit var adapter: ProductAdapter
    private var databaseReference: DatabaseReference? = null
    private var eventListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gridLayoutManager = GridLayoutManager(this@MainActivity, 2)
        binding.view1.layoutManager = gridLayoutManager

        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()

        dataList = ArrayList()
        adapter = ProductAdapter(this@MainActivity, dataList) { position ->
            showProductDetail(position)
        }

        binding.view1.adapter = adapter
        databaseReference = FirebaseDatabase.getInstance().getReference("product")
        dialog.show()

        eventListener = databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (itemSnapshot in snapshot.children) {
                    val product = itemSnapshot.getValue(Product::class.java)
                    product?.let { dataList.add(it) }
                }
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }

            override fun onCancelled(error: DatabaseError) {
                dialog.dismiss()
            }
        })

        binding.categorie1.setOnClickListener {
            filterByCategory("Sayur")
        }

        binding.categorie2.setOnClickListener {
            filterByCategory("Pelet")
        }

        binding.categorie3.setOnClickListener {
            filterByCategory("Buah")
        }

        binding.categorie4.setOnClickListener {
            filterByCategory("Pur")
        }

        binding.categorie5.setOnClickListener {
            filterByCategory("Beras")
        }

        // Dapatkan referensi ke TextView ViewAll
        val viewAllTextView: TextView = findViewById(R.id.ViewAll)

        // Tambahkan fungsi onClick
        viewAllTextView.setOnClickListener {
            // Arahkan pengguna ke MainActivity atau aktivitas lainnya
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        // Ambil data pengguna dari Firestore
        val firestore = FirebaseFirestore.getInstance()
        val userDocument = firestore.collection("users").document(currentUserUid.toString())

        userDocument.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val username = document.getString("username")
                    // Tampilkan username di TextView atau di mana pun yang diinginkan
                    binding.username.text = username
                } else {
                    // Handle jika dokumen tidak ditemukan
                }
            }
            .addOnFailureListener { exception ->
                // Handle kesalahan
            }


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle click on Home menu item
                    // Memulai ulang MainActivity jika belum berada di MainActivity
                        val intent = Intent(this@MainActivity, MainActivity::class.java)
                        startActivity(intent)
                        true
                }

                R.id.profile -> {
                    // Handle click on Profile menu item
                    // Start UploadActivity
                    val intent = Intent(this@MainActivity, AkunUserActivity::class.java)
                    startActivity(intent)
                    true
                }
                // ... (tambahkan penanganan item lain jika diperlukan)
                else -> false
            }
        }
    }

    private fun showProductDetail(position: Int) {
        val selectedProduct = dataList[position]
        Log.d("MainActivity", "Product ID yang dikirim: ${selectedProduct.id}")
        val intent = Intent(this@MainActivity, DetailProductActivity::class.java).apply {
            putExtra("PRODUCT_ID", selectedProduct.id)
            putExtra("PRODUCT_NAME", selectedProduct.nama)
            putExtra("PRODUCT_PRICE", selectedProduct.harga?.toString() ?: "0")
            putExtra("PRODUCT_IMAGE", selectedProduct.imageUrl)
            putExtra("JUMLAH_PRODUCT", selectedProduct.jumlah?.toString() ?: "0")
            putExtra("DESKRIPSI_PRODUCT", selectedProduct.deskripsi.orEmpty())
        }
        startActivity(intent)
    }

    private fun filterByCategory(category: String) {
        val filteredList = dataList.filter { it.category == category } as ArrayList<Product>
        adapter.setData(filteredList)
    }
}
