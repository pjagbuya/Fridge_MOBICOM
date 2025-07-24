package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewHolder.RecipeViewHolder

class RecipeMainAdapter(
    private val recipeList: ArrayList<RecipeModel>,
    private val onRecipeClick: (RecipeModel) -> Unit

) : RecyclerView.Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeNameTv.text = recipe.name

        holder.itemView.setOnClickListener {
            onRecipeClick(recipe)
        }

        // Edit button
        holder.editButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Edit clicked: ${recipe.name}", Toast.LENGTH_SHORT).show()
        }

        // Delete button
        holder.deleteButton.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Delete clicked: ${recipe.name}", Toast.LENGTH_SHORT).show()
        }
    }

    fun updateData(newRecipes: List<RecipeModel>) {
        this.recipeList.clear()
        this.recipeList.addAll(newRecipes)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = recipeList.size
}
