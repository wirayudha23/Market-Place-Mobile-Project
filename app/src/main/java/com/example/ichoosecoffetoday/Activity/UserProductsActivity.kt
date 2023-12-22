package com.example.ichoosecoffetoday.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ichoosecoffetoday.Adapter.UserProductAdapter
import com.example.ichoosecoffetoday.Domain.Product
import com.example.ichoosecoffetoday.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserProductsActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var userProductAdapter: UserProductAdapter
    private lateinit var productsList: MutableList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_products)

        recyclerView = findViewById(R.id.recyclerViewUserProducts)
        productsList = mutableListOf()
        userProductAdapter = UserProductAdapter(this, productsList) { selectedPosition ->
            val selectedProduct = productsList[selectedPosition]
            val intent = Intent(this, DetailProductActivity::class.java)
            intent.putExtra("product_id", selectedProduct.id)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userProductAdapter

        retrieveUserProducts()
    }

    private fun retrieveUserProducts() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        val productsRef = FirebaseDatabase.getInstance().getReference("product")

        productsRef.orderByChild("ownerUid").equalTo(currentUserUid).addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    productsList.clear()

                    for (productSnapshot in dataSnapshot.children) {
                        val product = productSnapshot.getValue(Product::class.java)
                        product?.let { productsList.add(it) }
                    }

                    userProductAdapter.setData(productsList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Firebase", "Gagal mendapatkan data produk", databaseError.toException())
                }
            }
        )
    }
}
