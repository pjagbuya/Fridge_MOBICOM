package com.mobdeve.agbuya.hallar.hong.fridge.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContainer(container: ContainerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(containers: List<ContainerEntity>)

    @Update
    suspend fun updateContainer(container: ContainerEntity)

    @Query("DELETE FROM ContainerEntity WHERE containerId = :containerId")
    suspend fun deleteContainerById(containerId: Int)

    @Delete
    suspend fun deleteContainer(container: ContainerEntity)

    @Query("DELETE FROM ContainerEntity")
    suspend fun deleteAll()

    @Query("SELECT * FROM ContainerEntity ORDER BY timeStamp DESC")
    fun getAllContainers(): Flow<List<ContainerEntity>>

    @Query("UPDATE ContainerEntity SET currCap = currCap + 1 WHERE containerId = :containerId")
    suspend fun increaseCurrCap(containerId: Int)

    @Query("UPDATE ContainerEntity SET currCap = currCap - 1 WHERE containerId = :containerId")
    suspend fun decreaseCurrCap(containerId: Int)

    @Query("SELECT name FROM ContainerEntity WHERE containerId = :containerId LIMIT 1")
    suspend fun getContainerNameById(containerId: Int): String?

    @Query("SELECT containerId, name FROM ContainerEntity WHERE ownerUserId = :userId")
    suspend fun getContainerIdNameMap(userId: Int): List<ContainerIdName>

    @Query("SELECT * FROM ContainerEntity WHERE ownerUserId = :userId ORDER BY timeStamp DESC")
    fun getAllContainersByUserId(userId: Int): Flow<List<ContainerEntity>>


    // Database searching:
    @Query("SELECT * FROM ContainerEntity WHERE name LIKE :searchQuery OR timeStamp LIKE :searchQuery")
    fun searchContainers(searchQuery: String): Flow<List<ContainerEntity>>
}

data class ContainerIdName(
    val containerId: Int,
    val name: String
)
