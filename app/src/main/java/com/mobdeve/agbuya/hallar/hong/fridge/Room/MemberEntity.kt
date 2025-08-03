package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.*

enum class InviteStatus {
    PENDING,
    ACCEPTED,
    DECLINED
}

@Entity()
data class MemberEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val fireAuthId: String,
    val memberAuthId: String,
    val nickname: String,
    val inviteStatus: String = "PENDING"
)