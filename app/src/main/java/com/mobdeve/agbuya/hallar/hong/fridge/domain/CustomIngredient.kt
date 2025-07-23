package com.mobdeve.agbuya.hallar.hong.fridge.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomIngredient(
    val name: String,
    val amount: Double,
    val unit: String // e.g., "tsp", "cup", "grams"
) : Parcelable