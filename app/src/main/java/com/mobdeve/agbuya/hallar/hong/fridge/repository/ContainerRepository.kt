package com.mobdeve.agbuya.hallar.hong.fridge.repository

import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContainerRepository @Inject constructor(
    private val containerDao: ContainerDao
) {
    val readAllData: Flow<List<ContainerEntity>> = containerDao.getAllContainers()

    suspend fun addContainer(container: ContainerEntity) = containerDao.insertContainer(container)
    suspend fun updateContainer(container: ContainerEntity) = containerDao.updateContainer(container)
    suspend fun deleteContainer(containerId: Int) = containerDao.deleteContainerById(containerId)
    suspend fun decreaseCurrCap(containerId: Int) = containerDao.decreaseCurrCap(containerId)

    suspend fun getContainerIdNameMap(userId: Int): List<ContainerIdName> =
        containerDao.getContainerIdNameMap(userId)

    fun searchDatabase(searchQuery : String) : Flow<List<ContainerEntity>> = containerDao.searchContainers(searchQuery)

    // New method for user-specific containers
    fun getContainersByFirebaseUid(firebaseUid: String): Flow<List<ContainerEntity>> {
        return containerDao.getContainersByFirebaseUid(firebaseUid)
    }

    // New method for container ID/name mapping by Firebase UID
    suspend fun getContainerIdNameMapByFirebaseUid(firebaseUid: String): List<ContainerIdName> {
        return containerDao.getContainerIdNameMapByFirebaseUid(firebaseUid)
    }

    // New search method by Firebase UID
    fun searchDatabaseByFirebaseUid(searchQuery: String, firebaseUid: String): Flow<List<ContainerEntity>> {
        return containerDao.searchContainersByFirebaseUid(searchQuery, firebaseUid)
    }
    suspend fun assignFireAuthId(authId: String?) {
        return containerDao.assignFireAuthId(authId)
    }
}