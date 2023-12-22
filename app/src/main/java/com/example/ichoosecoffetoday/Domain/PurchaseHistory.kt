package com.example.ichoosecoffetoday.Domain

data class PurchaseHistory(
    val id: Int,
    val userId: String,
    val productId: String,
    val productName: String,
    val productImageUrl: String?,
    val quantity: Int,
    val totalPrice: Double,
    val purchaseDate: String
)

