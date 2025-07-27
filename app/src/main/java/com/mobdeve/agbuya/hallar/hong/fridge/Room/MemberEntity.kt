package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

@Entity
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val inventoryId: Int,
    val memberEmail: String,
    val nickname: String,
    val inviteStatus: String = "PENDING" // default status
)
