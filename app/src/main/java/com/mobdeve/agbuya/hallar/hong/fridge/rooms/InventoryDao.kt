package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryWithContainers
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryWithEverything
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryWithRecipes


@Dao
interface InventoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInventory(inventory: InventoryEntity)

    @Query("SELECT * FROM inventories WHERE inventoryId = :inventoryId")
    suspend fun getInventoryById(inventoryId: String): InventoryEntity?

    @Query("SELECT * FROM inventories")
    fun getAllInventories(): LiveData<List<InventoryEntity>>

    @Query("SELECT * FROM inventories")
    suspend fun getAllInventories2(): List<InventoryEntity>

    @Transaction
    @Query("SELECT * FROM inventories WHERE inventoryId = :inventoryId")
    suspend fun getInventoryWithContainers(inventoryId: String): InventoryWithContainers

    @Transaction
    @Query("SELECT * FROM inventories WHERE inventoryId = :inventoryId")
    suspend fun getInventoryWithRecipes(inventoryId: String): InventoryWithRecipes

    @Transaction
    @Query("SELECT * FROM inventories WHERE inventoryId = :inventoryId")
    suspend fun getInventoryWithEverything(inventoryId: String): InventoryWithEverything

    @Delete
    suspend fun deleteInventory(inventory: InventoryEntity)

    @Query("DELETE FROM inventories")
    suspend fun deleteAll()
}
