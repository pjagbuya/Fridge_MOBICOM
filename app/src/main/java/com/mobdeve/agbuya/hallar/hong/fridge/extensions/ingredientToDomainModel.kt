package com.mobdeve.agbuya.hallar.hong.fridge.extensions

import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientEntity

fun IngredientEntity.toDomainModel(): Ingredient {
    return Ingredient(
        ingredientID = this.ingredientID,
        icon = this.iconResId, // or convert resId if needed
        name = this.name,
        quantity = this.quantity,
        price = this.price,
        unit = this.unit,
        conditionType = this.conditionType,
        itemType = this.itemType,
        dateAdded = this.dateAdded,
        expirationDate = this.expirationDate,
        imageContainerLists = this.imageList,
        attachedContainerID = this.attachedContainerId
    )
}