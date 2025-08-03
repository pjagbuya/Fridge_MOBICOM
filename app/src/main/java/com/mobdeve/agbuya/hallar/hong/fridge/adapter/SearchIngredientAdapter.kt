package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewHolder.AddIngredientViewHolder

class SearchIngredientAdapter(
    private var ingredientList: List<RecipeModel.RecipeIngredient>,
    private val onAddClick: (RecipeModel.RecipeIngredient) -> Unit
) : RecyclerView.Adapter<AddIngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddIngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_recipe_ingredient, parent, false)
        return AddIngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: AddIngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.ingredientNameTv.text = ingredient.name
        holder.addIngredientBtn.setOnClickListener {
            onAddClick(ingredient)
        }
    }
    override fun getItemCount(): Int = ingredientList.size

    fun updateData(newList: List<RecipeModel.RecipeIngredient>) {
        ingredientList = newList
        notifyDataSetChanged()
    }
}
