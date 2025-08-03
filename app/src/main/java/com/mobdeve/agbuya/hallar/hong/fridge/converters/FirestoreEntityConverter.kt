package com.mobdeve.agbuya.hallar.hong.fridge.converters

import android.util.Base64
import androidx.room.TypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.Room.MemberEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreMember
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreRecipe
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreRecipeIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreDataModels.FirestoreUser
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity


fun ImageContainer.toFirestoreImageContainer(): FirestoreImageContainer {
    return FirestoreImageContainer(
        id = 0,
        resId = this.getResId(),
        colorId = this.getColorId()
    )
}

fun FirestoreImageContainer.toImageContainer(): ImageContainer {
    return ImageContainer(resId = this.resId, colorId = this.colorId)
}


fun ImageRaw.toFirestoreImageRaw(): FirestoreImageRaw {
    val base64String = Base64.encodeToString(this.getBlob(), Base64.DEFAULT)
    return FirestoreImageRaw(
        base64 = base64String
    )
}

fun FirestoreImageRaw.toImageRaw(): ImageRaw {
    val byteArray = Base64.decode(this.base64, Base64.DEFAULT)
    return ImageRaw(byteArray)
}

fun ContainerEntity.toFirestoreContainer(): FirestoreContainer {
    return FirestoreContainer(
        containerId = this.containerId,
        name = this.name,
        imageContainer = this.imageContainer.toFirestoreImageContainer(),
        currCap = this.currCap,
        maxCap = this.maxCap,
        timeStamp = this.timeStamp,
        ownerUserId = this.ownerUserId,
        fireAuthId = this.fireAuthId
    )
}

fun FirestoreContainer.toContainerEntity(): ContainerEntity {
    return ContainerEntity(
        containerId = this.containerId,
        name = this.name,
        imageContainer = this.imageContainer.toImageContainer(),
        currCap = this.currCap,
        maxCap = this.maxCap,
        timeStamp = this.timeStamp,
        ownerUserId = this.ownerUserId,
        fireAuthId = this.fireAuthId
    )
}

fun IngredientEntity.toFirestoreIngredient(): FirestoreIngredient {
    return FirestoreIngredient(
        ingredientID = this.ingredientID,
        name = this.name,
        iconResId = this.iconResId,
        quantity = this.quantity,
        price = this.price,
        unit = this.unit,
        conditionType = this.conditionType,
        itemType = this.itemType,
        dateAdded = this.dateAdded,
        expirationDate = this.expirationDate,
        attachedContainerId = this.attachedContainerId,
        imageList = this.imageList.map { it.toFirestoreImageRaw() }
    )
}

fun FirestoreIngredient.toIngredientEntity(): IngredientEntity {
    return IngredientEntity(
        ingredientID = this.ingredientID,
        name = this.name,
        iconResId = this.iconResId,
        quantity = this.quantity,
        price = this.price,
        unit = this.unit,
        conditionType = this.conditionType,
        itemType = this.itemType,
        dateAdded = this.dateAdded,
        expirationDate = this.expirationDate,
        attachedContainerId = this.attachedContainerId,
        imageList = ArrayList(this.imageList.map { it.toImageRaw() })
    )
}

fun RecipeEntity.toFirestoreRecipe(): FirestoreRecipe {
    return FirestoreRecipe(
        id = this.id,
        userId = this.userId,
        userAuthId = this.userAuthId,
        name = this.name,
        description = this.description
    )
}

fun FirestoreRecipe.toRecipeEntity(): RecipeEntity {
    return RecipeEntity(
        id = this.id,
        userId = this.userId,
        userAuthId = this.userAuthId,
        name = this.name,
        description = this.description
    )
}

fun RecipeIngredientEntity.toFirestoreRecipeIngredient(): FirestoreRecipeIngredient {
    return FirestoreRecipeIngredient(
        ingredientId = this.ingredientId,
        recipeId = this.recipeId,
        name = this.name,
        amount = this.amount,
        unit = this.unit,
        isCustom = this.isCustom
    )
}

fun FirestoreRecipeIngredient.toRecipeIngredientEntity(): RecipeIngredientEntity {
    return RecipeIngredientEntity(
        ingredientId = this.ingredientId,
        recipeId = this.recipeId,
        name = this.name,
        amount = this.amount,
        unit = this.unit,
        isCustom = this.isCustom
    )
}

fun MemberEntity.toFirestoreMember(): FirestoreMember =
    FirestoreMember(
        fireAuthId = this.fireAuthId,
        memberAuthId = this.memberAuthId,
        nickname = this.nickname,
        inviteStatus = this.inviteStatus
    )

fun FirestoreMember.toMemberEntity(): MemberEntity =
    MemberEntity(
        fireAuthId = this.fireAuthId,
        memberAuthId = this.memberAuthId,
        nickname = this.nickname,
        inviteStatus = this.inviteStatus
    )

@TypeConverter
fun UserEntity.toFirestoreUser(): FirestoreUser =
    FirestoreUser(
        name = this.name,
        fireAuthId = this.fireAuthId
    )

@TypeConverter
fun FirestoreUser.toUserEntity(): UserEntity =
    UserEntity(
        id = 0,
        name = this.name,
        fireAuthId = this.fireAuthId
    )

