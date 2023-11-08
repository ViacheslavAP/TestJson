package com.example.testjson.retrofit

data class ProductModel(
    val id: Int,
    var title: String,
    val descriptions: String,
    val price: Int,
    val discountPercentage: Double,
    val rating: Double,
    val stock: Int,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)
