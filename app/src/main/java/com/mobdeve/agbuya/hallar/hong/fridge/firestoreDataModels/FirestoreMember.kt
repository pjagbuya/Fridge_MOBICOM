package com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels

data class FirestoreMember(
    val fireAuthId: String = "",        // who invited
    val memberAuthId: String = "",      // who is invited
    val nickname: String = "",
    val inviteStatus: String = "PENDING" // store as string
)
