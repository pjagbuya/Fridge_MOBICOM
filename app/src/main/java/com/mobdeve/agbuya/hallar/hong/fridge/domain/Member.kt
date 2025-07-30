package com.mobdeve.agbuya.hallar.hong.fridge.domain

import kotlinx.serialization.Serializable

@Serializable
data class Member(
    val firebaseUid: String,
    val username: String,
    val role: String
)