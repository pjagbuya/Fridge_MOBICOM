package com.mobdeve.agbuya.hallar.hong.fridge.extensions

import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerEntity

fun ContainerEntity.toDomain(): ContainerModel {
    return ContainerModel(
        this.containerId,
        name = this.name,
        imageContainer = this.imageContainer,
        currCap = this.currCap,
        maxCap = this.maxCap,
        timeStamp = this.timeStamp,
        inventoryOwnerUserId = this.inventoryOwnerId,
    )
}

fun ContainerModel.toEntity(ownerUserId: Int, containerId: Int = 0): ContainerEntity {
    return ContainerEntity(
        containerId = containerId,
        name = this.name,
        imageContainer = this.imageContainer,
        currCap = this.currCap,
        maxCap = this.maxCap,
        timeStamp = this.timeStamp,
        inventoryOwnerId = this.inventoryOwnerUserId
    )
}