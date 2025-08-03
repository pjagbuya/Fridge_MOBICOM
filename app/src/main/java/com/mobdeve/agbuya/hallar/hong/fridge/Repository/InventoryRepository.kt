package com.mobdeve.agbuya.hallar.hong.fridge.Repository

import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InviteStatus
import com.mobdeve.agbuya.hallar.hong.fridge.Room.MemberEntity

class InventoryRepository(private val dao: InventoryDao) {
    suspend fun createInventory(userId: Int, userAuthId: String, name: String, inventoryId: String): Long {
        return dao.insertInventory(InventoryEntity(userId = userId, userAuthId = userAuthId, name = name, inventoryId = inventoryId))
    }

//    suspend fun inviteMember(inventoryId: String, userAuthId: String, nickname: String, inviteStatus: InviteStatus): Result<Long> {
//        val user = dao.getUserByfireAuthId(userAuthId) ?: return Result.failure(Exception("User does not exist"))
//
//        val currentMembers = dao.getMembers(inventoryId)
//        if (currentMembers.size >= 5) {
//            return Result.failure(Exception("Maximum members reached"))
//        }
//
//        val id = dao.insertMember(MemberEntity(
//            inventoryId = inventoryId,
//            nickname = nickname,
//            userAuthId = userAuthId,
//            inviteStatus = inviteStatus
//        ))
//        return Result.success(id)
//    }

//    suspend fun updateInviteStatus(memberId: Int, status: String) {
//        dao.updateInviteStatus(status, memberId)
//    }

    suspend fun getInventoryByName(name: String): InventoryEntity? {
        return dao.getInventoryByName(name)
    }

    suspend fun getOwnerInventory(): InventoryEntity? {
        return dao.getOwnerInventory("inv_sample_001")
    }
}
