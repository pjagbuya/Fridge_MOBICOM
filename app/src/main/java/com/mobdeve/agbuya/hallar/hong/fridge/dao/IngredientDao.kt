package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

@Dao
interface IngredientDao {
    @Insert
    suspend fun insertIngredient(ingredient: IngredientEntity)

    @Update
    suspend fun updateIngredient(ingredient: IngredientEntity)

    @Delete
    suspend fun deleteIngredient(ingredient: IngredientEntity)

    @Query("DELETE FROM IngredientEntity WHERE ingredientId = :ingredientId")
    suspend fun deleteIngredientById(ingredientId: Int)
    @Query("DELETE FROM IngredientEntity WHERE attachedContainerId = :containerId")
    suspend fun deleteAllIngredientsByContainerId(containerId: Int)

    @Query("SELECT * FROM IngredientEntity WHERE attachedContainerId = :containerId")
    suspend fun getIngredientsByContainer(containerId: Int): List<IngredientEntity>
    @Query("SELECT * FROM IngredientEntity ORDER BY ingredientID DESC")
    fun getAllIngredients(): LiveData<List<IngredientEntity>>
    @Transaction
    suspend fun insertAndUpdateCapacity(ingredient: IngredientEntity) {
        insertIngredient(ingredient)
        increaseContainerCap(ingredient.attachedContainerId)
    }

    @Query("UPDATE ContainerEntity SET currCap = currCap + 1 WHERE containerId = :containerId")
    suspend fun increaseContainerCap(containerId: Int)

    @Query("UPDATE ContainerEntity SET currCap = currCap - 1 WHERE containerId = :containerId")
    suspend fun decreaseContainerCap(containerId: Int)


    @Query("DELETE FROM IngredientEntity")
    suspend fun deleteAll()

    //TODO : This should be the official groceryShareviewmodel get All once a user session is decided
    @Query("SELECT * FROM IngredientEntity WHERE attachedContainerId IN (:containerIds)")
    suspend fun getIngredientsByContainerIds(containerIds: List<Int>): List<IngredientEntity>


    // Features
    @Query("SELECT * FROM IngredientEntity ORDER BY dateAdded DESC")
    fun getAllIngredientsSortedByDateDesc(): LiveData<List<IngredientEntity>>

    @Query("SELECT * FROM IngredientEntity ORDER BY dateAdded ASC")
    fun getAllIngredientsSortedByDateAsc(): LiveData<List<IngredientEntity>>
}