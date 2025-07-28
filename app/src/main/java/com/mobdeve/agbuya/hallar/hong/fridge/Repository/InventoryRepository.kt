package com.mobdeve.agbuya.hallar.hong.fridge.Repository

import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.MemberEntity

class InventoryRepository(private val dao: InventoryDao) {
    suspend fun createInventory(ownerEmail: String, name: String): Long {
        return dao.insertInventory(InventoryEntity(ownerEmail = ownerEmail, name = name))
    }

    suspend fun inviteMember(inventoryId: Int, email: String, nickname: String): Result<Long> {
        val user = dao.getUserByEmail(email) ?: return Result.failure(Exception("User does not exist"))

        val currentMembers = dao.getMembers(inventoryId)
        if (currentMembers.size >= 5) {
            return Result.failure(Exception("Maximum members reached"))
        }

        val id = dao.insertMember(MemberEntity(
            inventoryId = inventoryId,
            memberEmail = email,
            nickname = nickname
        ))
        return Result.success(id)
    }

//    suspend fun updateInviteStatus(memberId: Int, status: String) {
//        dao.updateInviteStatus(status, memberId)
//    }

    suspend fun getInventoryByName(name: String): InventoryEntity? {
        return dao.getInventoryByName(name)
    }
}
