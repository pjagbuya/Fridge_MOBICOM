package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.content.Context
import android.net.Uri
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.ItemType
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.ConditionType
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient.Companion.UnitOfMeasurement
import androidx.core.net.toUri


class GroceryDataHelper {
    companion object {
        fun getSampleIngredients(context: Context): ArrayList<Ingredient> {
            val ingredients = ArrayList<Ingredient>()

            fun createImageIngredient(context: Context): ImageIngredient {
                return ImageIngredient(
                    uri = "android.resource://${context.packageName}/${R.drawable.qr_icon}".toUri(),
                    name = "qr_icon",
                    context = context
                )
            }

            ingredients.add(
                Ingredient(
                    ingredientID = 1,
                    icon = createImageIngredient(context),
                    name = "Tomato",
                    amount = 4.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2025-07-07",
                    unit = UnitOfMeasurement.PIECE,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.VEGETABLE
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 2,
                    icon = createImageIngredient(context),
                    name = "Eggs",
                    amount = 12.0,
                    dateAdded = "2025-07-02",
                    expirationDate = "2025-07-12",
                    unit = UnitOfMeasurement.DOZEN,
                    conditionType = ConditionType.STILL_OK,
                    itemType = ItemType.EGG
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 3,
                    icon = createImageIngredient(context),
                    name = "Milk",
                    amount = 1.0,
                    dateAdded = "2025-07-05",
                    expirationDate = "2025-07-10",
                    unit = UnitOfMeasurement.LITER,
                    conditionType = ConditionType.SLIGHTLY_NOT_OK,
                    itemType = ItemType.MILK
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 4,
                    icon = createImageIngredient(context),
                    name = "Rice",
                    amount = 2.5,
                    dateAdded = "2025-07-01",
                    expirationDate = "2025-12-01",
                    unit = UnitOfMeasurement.KILOGRAM,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.RICE
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 5,
                    icon = createImageIngredient(context),
                    name = "Chicken Breast",
                    amount = 1.2,
                    dateAdded = "2025-07-03",
                    expirationDate = "2025-07-08",
                    unit = UnitOfMeasurement.KILOGRAM,
                    conditionType = ConditionType.STILL_OK,
                    itemType = ItemType.MEAT
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 6,
                    icon = createImageIngredient(context),
                    name = "Pasta",
                    amount = 1.0,
                    dateAdded = "2025-06-30",
                    expirationDate = "2026-01-01",
                    unit = UnitOfMeasurement.BOX,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.PASTA
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 7,
                    icon = createImageIngredient(context),
                    name = "Cooking Oil",
                    amount = 0.75,
                    dateAdded = "2025-07-01",
                    expirationDate = "2026-07-01",
                    unit = UnitOfMeasurement.LITER,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.OIL
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 8,
                    icon = createImageIngredient(context),
                    name = "Soy Sauce",
                    amount = 1.0,
                    dateAdded = "2025-07-01",
                    expirationDate = "2026-01-01",
                    unit = UnitOfMeasurement.BOTTLE,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.SAUCE
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 9,
                    icon = createImageIngredient(context),
                    name = "Salt",
                    amount = 500.0,
                    dateAdded = "2025-06-20",
                    expirationDate = "2027-06-20",
                    unit = UnitOfMeasurement.GRAM,
                    conditionType = ConditionType.VERY_OK,
                    itemType = ItemType.SALT
                )
            )

            ingredients.add(
                Ingredient(
                    ingredientID = 10,
                    icon = createImageIngredient(context),
                    name = "Apple",
                    amount = 3.0,
                    dateAdded = "2025-07-04",
                    expirationDate = "2025-07-09",
                    unit = UnitOfMeasurement.PIECE,
                    conditionType = ConditionType.STILL_OK,
                    itemType = ItemType.FRUIT
                )
            )

            return ingredients
        }
    }
}