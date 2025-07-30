package com.mobdeve.agbuya.hallar.hong.fridge.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContainer(container: ContainerEntity) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(containers: List<ContainerEntity>)

    @Update
    suspend fun updateContainer(container: ContainerEntity)

    @Delete
    suspend fun deleteContainer(container: ContainerEntity)

    @Query("DELETE FROM ContainerEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM ContainerEntity ORDER BY timeStamp DESC")
    fun getAllContainers(): LiveData<List<ContainerEntity>>
//
//    @Query("SELECT * FROM ContainerEntity WHERE containerId = :selectedId LIMIT 1")
//    suspend fun getContainerById(selectedId: Int): ContainerModel?
}