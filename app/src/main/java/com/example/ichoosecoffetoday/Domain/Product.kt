package com.example.ichoosecoffetoday.Domain

data class Product(
    val id: String = "",
    val nama: String? = null,
    val harga: Int? = null,
    val jumlah: Int? = null,
    val deskripsi: String? = null,
    val imageUrl: String? = null,
    val category: String = "",
    val ownerUid: String = "", // Tambahkan field ownerUid

) {
    constructor() : this("", "", 0, 0, "", "", "", "")
}
