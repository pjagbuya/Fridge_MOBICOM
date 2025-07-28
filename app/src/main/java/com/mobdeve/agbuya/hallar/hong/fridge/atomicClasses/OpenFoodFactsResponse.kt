package com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses

import com.google.gson.annotations.SerializedName

// OpenFoodFactsResponse.kt
data class OpenFoodFactsResponse(
    val product: Product?,
    val status: Int,
    val code: String
)

data class Product(
    @SerializedName("product_name_en")
    val productNameEN: String?,

    @SerializedName("product_quantity")
    val quantity: String?,

    @SerializedName("product_quantity_unit")
    val unit: String?,

    @SerializedName("expiration_date")
    val expirationDate: String?
)