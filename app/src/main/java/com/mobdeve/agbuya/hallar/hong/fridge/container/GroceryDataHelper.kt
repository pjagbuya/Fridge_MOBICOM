package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.ItemType
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.ConditionType
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.UnitOfMeasurement
import androidx.core.net.toUri


class GroceryDataHelper {
    companion object {
        fun getSampleIngredients(context: Context): ArrayList<Ingredient> {
            val ingredients = ArrayList<Ingredient>()

            // double exclamations assert that this must not be non-null
            fun createImageList(context: Context): ArrayList<ImageRaw> {
                val list = ArrayList<ImageRaw>()
                val uri = Uri.parse("android.resource://${context.packageName}/${R.drawable.hutao_only}")
                val dummyImage = ImageRaw.extractBitmap(context, uri)
                    ?: throw IllegalArgumentException("Drawable not found: hutao_only")
                repeat(4) {
                    list.add(ImageRaw(dummyImage))
                }
                return list
            }

            fun createImageIngredient(context: Context): Int {
                val uri = Uri.parse("android.resource://${context.packageName}/${R.drawable.hutao_only}")
                val dummy_image = R.mipmap.chef_hat_icon
                    ?: throw IllegalArgumentException("Drawable not found: qr_icon")
                return dummy_image
            }


            ingredients.add(
                Ingredient(
                    ingredientID = 1,
                    icon = createImageIngredient(context),
                    name = "Tomato",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2025-07-07",
                    unit = UnitOfMeasurement.PIECE.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.VEGETABLE.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 2,
                    icon = createImageIngredient(context),
                    name = "Eggs",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-02",
                    expirationDate = "2025-07-12",
                    unit = UnitOfMeasurement.DOZEN.displayName,
                    conditionType = ConditionType.STILL_OK.displayName,
                    itemType = ItemType.EGG.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 3,
                    icon = createImageIngredient(context),
                    name = "Milk",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-05",
                    expirationDate = "2025-07-10",
                    unit = UnitOfMeasurement.LITER.displayName,
                    conditionType = ConditionType.SLIGHTLY_NOT_OK.displayName,
                    itemType = ItemType.MILK.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0

                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 4,
                    icon = createImageIngredient(context),
                    name = "Rice",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2025-12-01",
                    unit = UnitOfMeasurement.KILOGRAM.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.RICE.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 5,
                    icon = createImageIngredient(context),
                    name = "Chicken Breast",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-03",
                    expirationDate = "2025-07-08",
                    unit = UnitOfMeasurement.KILOGRAM.displayName,
                    conditionType = ConditionType.STILL_OK.displayName,
                    itemType = ItemType.MEAT.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 6,
                    icon = createImageIngredient(context),
                    name = "Pasta",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-06-30",
                    expirationDate = "2026-01-01",
                    unit = UnitOfMeasurement.BOX.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.PASTA.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 7,
                    icon = createImageIngredient(context),
                    name = "Cooking Oil",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2026-07-01",
                    unit = UnitOfMeasurement.LITER.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.OIL.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 8,
                    icon = createImageIngredient(context),
                    name = "Soy Sauce",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2026-01-01",
                    unit = UnitOfMeasurement.BOTTLE.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.SAUCE.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 9,
                    icon = createImageIngredient(context),
                    name = "Salt",
                    quantity = 3.0,
                    price = 500.0,
                    dateAdded = "2025-06-20",
                    expirationDate = "2027-06-20",
                    unit = UnitOfMeasurement.GRAM.displayName,
                    conditionType = ConditionType.VERY_OK.displayName,
                    itemType = ItemType.SALT.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 10,
                    icon = createImageIngredient(context),
                    name = "Apple",
                    quantity = 3.0,
                    price = 10.0,
                    dateAdded = "2025-07-04",
                    expirationDate = "2025-07-09",
                    unit = UnitOfMeasurement.PIECE.displayName,
                    conditionType = ConditionType.STILL_OK.displayName,
                    itemType = ItemType.FRUIT.displayName,
                    imageContainerLists = createImageList(context),
                    attachedContainerID = 0
                )
            )

            return ingredients
        }
    }
}