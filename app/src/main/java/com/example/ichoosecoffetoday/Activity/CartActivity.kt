package com.example.ichoosecoffetoday.Activity

import android.content.Intent
import java.text.DecimalFormat
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.ichoosecoffetoday.Domain.Product
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.databinding.ActivityCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.example.ichoosecoffetoday.Activity.MainActivity


class CartActivity : AppCompatActivity() {

    private val decimalFormat = DecimalFormat("#,###.##")
    private lateinit var binding: ActivityCartBinding
    private var jumlahItem = 1
    private var hargaProduk = 0.0
    private var jumlahProduct = 0
    private var productId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menerima data dari intent
        productId = intent.getStringExtra("PRODUCT_ID")
        Log.d("CartActivity", "Product ID yang diterima: $productId")
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productPrice = intent.getStringExtra("PRODUCT_PRICE")
        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        jumlahProduct = intent.getIntExtra("JUMLAH_PRODUCT", 0)
        val deskripsiProduct = intent.getStringExtra("DESKRIPSI_PRODUCT")

        getUserAddressFromFirestore()

        binding.namaProductCart.text = productName
        binding.hargaProductCart.text = "Rp. $productPrice"
        binding.stockProductCart.text = "Stock : $jumlahProduct"
        Glide.with(this).load(productImage).into(binding.gambarCart)

        hargaProduk = productPrice?.toDoubleOrNull() ?: 0.0

        binding.hargaProductCart.text = formatCurrency(hargaProduk)
        updateSubTotal()

        binding.plusCartButton.setOnClickListener {
            incrementItem()
        }

        binding.minusCartBtn.setOnClickListener {
            decrementItem()
        }

        binding.buttonBeliCart.setOnClickListener {
            // Panggil fungsi untuk mengurangi stok dan memperbarui jumlah di Realtime Database
            updateStockAndQuantity(productId, jumlahProduct, jumlahItem)
        }
    }

    private fun updateStockAndQuantity(productId: String?, currentStock: Int, selectedQuantity: Int) {
        val newStock = currentStock - selectedQuantity

        // Periksa jika stok mencukupi
        if (newStock >= 0) {
            // Update nilai stok produk di Firebase Realtime Database
            updateStockInFirebase(productId, newStock)

            // Hitung dan gunakan hasil pengurangan jumlah dengan selectedQuantity untuk memperbarui Realtime Database
            val updatedQuantity = jumlahProduct - selectedQuantity

            // Panggil fungsi untuk memperbarui nilai jumlah di Realtime Database
            updateQuantityInFirebase(productId!!, updatedQuantity)

            // Tempatkan logika lanjutan di sini jika diperlukan (contoh: navigasi ke halaman terimakasih)
            // ...

            // Tampilkan pesan atau lakukan tindakan sesuai kebutuhan
            Toast.makeText(this, "Pembelian berhasil", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Tampilkan pesan bahwa stok tidak mencukupi
            Toast.makeText(this, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuantityInFirebase(productId: String, updatedQuantity: Int) {
        val database = FirebaseDatabase.getInstance()
        val productRef = database.getReference("product").child(productId)

        // Memperbarui nilai jumlah produk di Firebase Realtime Database
        productRef.child("jumlah").setValue(updatedQuantity)
            .addOnSuccessListener {
                // Jumlah berhasil diperbarui
                Toast.makeText(this, "Jumlah produk diperbarui", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Gagal memperbarui jumlah
                Toast.makeText(this, "Gagal memperbarui jumlah: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateStockInFirebase(productId: String?, newStock: Int) {
        if (productId != null) {
            val database = FirebaseDatabase.getInstance()
            val productRef = database.getReference("product").child(productId)

            // Memperbarui nilai stok produk di Firebase Realtime Database
            productRef.child("jumlah").setValue(newStock)
                .addOnSuccessListener {
                    // Stok berhasil diperbarui
                    Log.d("CartActivity", "Stok produk diperbarui ke $newStock")
                }
                .addOnFailureListener { e ->
                    // Gagal memperbarui stok
                    Log.e("CartActivity", "Gagal memperbarui stok: ${e.message}")
                }
        } else {
            // Handle ketika productId null
            // ...
        }
    }



    private fun incrementItem() {
        if (jumlahItem < jumlahProduct) {
            jumlahItem++
            updateSubTotal()
        } else {
            // Tampilkan pesan atau ambil tindakan lain sesuai kebutuhan
            Toast.makeText(this, "Stock item tidak mencukupi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun decrementItem() {
        if (jumlahItem > 1) {
            jumlahItem--
            updateSubTotal()
        }
    }

    private fun updateSubTotal() {
        binding.numberItemTxt.text = jumlahItem.toString()

        updateStockInFirebase(productId, jumlahProduct - jumlahItem)

        val subTotalValue = hargaProduk * jumlahItem
        binding.subTotal.text = formatCurrency(subTotalValue)

        val feeDeliveryValue = 10000.0
        binding.feeDelivery.text = formatCurrency(feeDeliveryValue)

        val totalOrderValue = subTotalValue + feeDeliveryValue
        binding.totalOrder.text = formatCurrency(totalOrderValue)
    }

    private fun formatCurrency(value: Double): String {
        val formattedValue = decimalFormat.format(value)
        return if (formattedValue.endsWith(".00")) {
            formattedValue.substring(0, formattedValue.length - 3)
        } else {
            formattedValue
        }
    }

    private fun getUserAddressFromFirestore() {
        // Mendapatkan referensi ke Firestore
        val db = FirebaseFirestore.getInstance()

        // Mendapatkan UID pengguna yang sedang login
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Pastikan userId tidak null sebelum mengakses Firestore
        userId?.let {
            // Mendapatkan dokumen pengguna dari Firestore
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // Dokumen pengguna ditemukan, ambil alamat
                        val userAddress = documentSnapshot.getString("alamat") ?: "Alamat Tidak Tersedia"

                        // Atur teks pada TextView dengan id addressOrder
                        binding.addressOrder.text = userAddress
                    } else {
                        // Dokumen pengguna tidak ditemukan
                        binding.addressOrder.text = "Alamat Tidak Tersedia"
                    }
                }
                .addOnFailureListener { e ->
                    // Gagal mengakses Firestore
                    binding.addressOrder.text = "Gagal mengambil alamat: ${e.message}"
                }
        }
    }
}
