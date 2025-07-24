package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Update
    suspend fun updateIngredient(ingredient: IngredientEntity)

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)

    @Query("SELECT * FROM IngredientEntity WHERE attachedContainerId = :containerId")
    suspend fun getIngredientsByContainer(containerId: Int): List<IngredientEntity>
    @Query("SELECT * FROM IngredientEntity")
    suspend fun getAllIngredients(): List<IngredientEntity>
    @Transaction
    suspend fun insertAndUpdateCapacity(ingredient: IngredientEntity) {
        insertIngredient(ingredient)
        increaseContainerCap(ingredient.attachedContainerId)
    }

    @Query("UPDATE ContainerEntity SET currCap = currCap + 1 WHERE containerId = :containerId")
    suspend fun increaseContainerCap(containerId: Int)

    @Query("UPDATE ContainerEntity SET currCap = currCap - 1 WHERE containerId = :containerId")
    suspend fun decreaseContainerCap(containerId: Int)
}