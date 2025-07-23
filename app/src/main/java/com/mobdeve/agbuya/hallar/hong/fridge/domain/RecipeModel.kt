package com.mobdeve.agbuya.hallar.hong.fridge.domain

import android.os.Parcelable
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    var id: Int = generateRecipeId(),
    var name: String,
    var description: String = "",
    var ingredients: ArrayList<RecipeIngredient> = ArrayList()
) : Parcelable {

    @Parcelize
    data class RecipeIngredient(
        val ingredientID: Int = generateIngredientId(), // Auto-increment ID
        val name: String,
        var amount: Double,
        var unit: RecipeUnit = RecipeUnit.UNSPECIFIED,
        val isCustom: Boolean = false
    ) : Parcelable

    enum class RecipeUnit(val displayName: String) {
        TSP("tsp"),
        TBSP("tbsp"),
        CUP("cup"),
        ML("ml"),
        L("L"),
        PIECE("piece"),
        GRAM("gram"),
        KG("kg"),
        UNSPECIFIED("");

        override fun toString(): String = displayName
    }

    companion object {
        // Counters for auto-increment
        private var recipeCounter = 0
        private var ingredientCounter = 0

        private fun generateRecipeId(): Int {
            recipeCounter += 1
            return recipeCounter
        }

        private fun generateIngredientId(): Int {
            ingredientCounter += 1
            return ingredientCounter
        }

        fun fromIngredient(
            ingredient: Ingredient,
            amount: Double,
            unit: RecipeUnit
        ): RecipeIngredient {
            return RecipeIngredient(
                name = ingredient.name,
                amount = amount,
                unit = unit
            )
        }
    }
}
