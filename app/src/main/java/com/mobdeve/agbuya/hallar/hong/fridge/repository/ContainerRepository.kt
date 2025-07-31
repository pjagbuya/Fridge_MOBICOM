package com.mobdeve.agbuya.hallar.hong.fridge.repository

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity

class ContainerRepository(private val containerDao: ContainerDao) {
    val readAllData : LiveData<List<ContainerEntity>> = containerDao.getAllContainers()

    suspend fun addContainer(container : ContainerEntity){
        containerDao.insertContainer(container)
    }
    suspend fun deleteContainer(containerId: Int){
        containerDao.deleteContainerById(containerId)
    }
    suspend fun updateContainer(container: ContainerEntity){
        containerDao.updateContainer(container)
    }
    suspend fun decreaseCurrCap(containerId: Int){
        containerDao.decreaseCurrCap(containerId)
    }

    suspend fun getContainerIdNameMap(userId: Int): List<ContainerIdName> {
        return containerDao.getContainerIdNameMap(userId)
    }
}