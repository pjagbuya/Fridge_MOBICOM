package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Transaction
    suspend fun insertAndUpdateCapacity(ingredient: IngredientEntity) {
        insertIngredient(ingredient)
        increaseContainerCap(ingredient.attachedContainerId)
    }

    @Query("UPDATE ContainerEntity SET currCap = currCap + 1 WHERE containerId = :containerId")
    suspend fun increaseContainerCap(containerId: Int)
}