package com.example.ichoosecoffetoday.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.ichoosecoffetoday.databinding.ActivityDetailProductBinding
import android.os.Parcelable
import android.util.Log
import com.example.ichoosecoffetoday.R


class DetailProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari intent
        val productId = intent.getStringExtra("PRODUCT_ID")
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productPrice = intent.getStringExtra("PRODUCT_PRICE")
        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        val jumlahProduct = intent.getStringExtra("JUMLAH_PRODUCT")
        val deskripsiProduct = intent.getStringExtra("DESKRIPSI_PRODUCT")

        // Tampilkan data pada UI
        binding.namaProductTxt.text = productName
        binding.hargaProductTxt.text = "Rp. $productPrice"
        binding.jumlahProductTxt.text = jumlahProduct
        binding.deskripsiProductTxt.text = deskripsiProduct
        Glide.with(this).load(productImage).into(binding.retrieveImage)

        binding.buttonBuy.setOnClickListener {
            // Memeriksa apakah jumlah item yang dimasukkan melebihi stok
            val jumlahProductInt = jumlahProduct?.toIntOrNull() ?: 0

            if (jumlahProductInt > 0) {
                // Membuat Intent
                val intent = Intent(this, CartActivity::class.java).apply {
                    putExtra("PRODUCT_ID", productId)
                    putExtra("PRODUCT_NAME", productName)
                    putExtra("PRODUCT_PRICE", productPrice)
                    putExtra("PRODUCT_IMAGE", productImage)
                    putExtra("JUMLAH_PRODUCT", jumlahProductInt) // Menggunakan jumlahProductInt
                    putExtra("DESKRIPSI_PRODUCT", deskripsiProduct)
                }

                Log.d("DetailProductActivity", "Product ID yang dikirim: $productId")
                startActivity(intent)
            } else {

            }
        }
    }
}
