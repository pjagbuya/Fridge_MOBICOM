package com.mobdeve.agbuya.hallar.hong.fridge.repository

import androidx.lifecycle.LiveData
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerEntity

class ContainerRepository(private val containerDao: ContainerDao) {
    val readAllData : LiveData<List<ContainerEntity>> = containerDao.getAllContainers()

    suspend fun addContainer(container : ContainerEntity){
        containerDao.insertContainer(container)
    }

    suspend fun updateContainer(container: ContainerEntity){
        containerDao.updateContainer(container)
    }

}