package com.mobdeve.agbuya.hallar.hong.fridge.domain

import android.os.Parcelable
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeModel(
    var id: Int,
    var name: String,
    var description: String = "",
    var ingredients: ArrayList<RecipeIngredient> = ArrayList()
) : Parcelable {

    /**
     * Inner class for recipe-specific ingredients
     * (Can be custom or from Ingredient model)
     */
    @Parcelize
    data class RecipeIngredient(
        val ingredientID: String? = null, // If null, this means custom ingredient
        val name: String,
        var amount: Double,
        var unit: RecipeUnit = RecipeUnit.UNSPECIFIED,
        val isCustom: Boolean = false // True if added manually
    ) : Parcelable

    /**
     * Enum for units specific to recipes
     */
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

    //to list the ingredients to RecipeIngredients but only the name
    companion object {
        fun fromIngredient(
            ingredient: Ingredient,
            amount: Double,
            unit: RecipeUnit
        ): RecipeIngredient {
            return RecipeIngredient(
                ingredientID = ingredient.ingredientID,
                name = ingredient.name,
                amount = amount,
                unit = unit
            )
        }
    }
}
