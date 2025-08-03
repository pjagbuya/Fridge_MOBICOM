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

    @Query("SELECT * FROM UserEntity WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM UserEntity WHERE fireAuthId = :userAuthId")
    suspend fun getUserByfireAuthId(userAuthId: String): UserEntity?

//    @Query("SELECT * FROM MemberEntity WHERE userAuthId = :fireAuthId")
//    suspend fun getMembers(userAuthId: String ): List<MemberEntity>


//    @Query("UPDATE MemberEntity SET inviteStatus = :status WHERE id = :memberId")
//    suspend fun updateInviteStatus(status: String, memberId: Int)

    @Query("SELECT * FROM InventoryEntity WHERE name = :name LIMIT 1")
    suspend fun getInventoryByName(name: String): InventoryEntity?

    @Query("SELECT * FROM InventoryEntity WHERE inventoryId =:inventoryId LIMIT 1")
    suspend fun getOwnerInventory(inventoryId: String): InventoryEntity?
}
