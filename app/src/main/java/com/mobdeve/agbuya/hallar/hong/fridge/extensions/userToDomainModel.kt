package com.mobdeve.agbuya.hallar.hong.fridge.extensions

import com.mobdeve.agbuya.hallar.hong.fridge.domain.User
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.UserEntity

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        username = this.username,
        password = this.password
    )
}