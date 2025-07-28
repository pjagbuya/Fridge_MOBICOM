package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface InventoryDao {
    @Insert
    suspend fun insertInventory(inventory: InventoryEntity): Long

    @Insert
    suspend fun insertMember(member: MemberEntity): Long

    @Query("SELECT * FROM UserEntity WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM MemberEntity WHERE inventoryId = :inventoryId")
    suspend fun getMembers(inventoryId: Int): List<MemberEntity>

    @Query("UPDATE MemberEntity SET inviteStatus = :status WHERE id = :memberId")
    suspend fun updateInviteStatus(memberId: Int, status: String)

    @Query("SELECT * FROM InventoryEntity WHERE name = :name LIMIT 1")
    suspend fun getInventoryByName(name: String): InventoryEntity?
}
