package com.mobdeve.agbuya.hallar.hong.fridge.repository


import androidx.lifecycle.LiveData
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryWithContainers
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryWithEverything
import com.mobdeve.agbuya.hallar.hong.fridge.Room.InventoryWithRecipes

class InventoryRepository(private val inventoryDao: InventoryDao) {

    val readAllData : LiveData<List<InventoryEntity>> = inventoryDao.getAllInventories()

    suspend fun getAllInventories(): List<InventoryEntity> =
        inventoryDao.getAllInventories2()

    suspend fun getInventoryWithContainers(id: String): InventoryWithContainers =
        inventoryDao.getInventoryWithContainers(id)

    suspend fun getInventoryWithRecipes(id: String): InventoryWithRecipes =
        inventoryDao.getInventoryWithRecipes(id)

    suspend fun getInventoryWithEverything(id: String): InventoryWithEverything =
        inventoryDao.getInventoryWithEverything(id)

    suspend fun insertInventory(inventory: InventoryEntity) {
        inventoryDao.insertInventory(inventory)
    }

    suspend fun deleteInventory(inventory: InventoryEntity) {
        inventoryDao.deleteInventory(inventory)
    }
}
