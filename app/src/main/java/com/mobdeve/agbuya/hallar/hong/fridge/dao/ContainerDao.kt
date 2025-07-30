package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerWithIngredients

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContainer(container: ContainerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(containers: List<ContainerEntity>)

    @Update
    suspend fun updateContainer(container: ContainerEntity)

    @Delete
    suspend fun deleteContainer(container: ContainerEntity)

    @Query("SELECT * FROM containers ORDER BY timeStamp DESC")
    fun getAllContainers(): LiveData<List<ContainerEntity>>

    @Transaction
    @Query("SELECT * FROM containers WHERE containerId = :containerId")
    suspend fun getContainerWithIngredients(containerId: Int): ContainerWithIngredients

    @Query("DELETE FROM containers")
    suspend fun deleteAll()
}
