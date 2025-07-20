package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.RecipeViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter

class RecipeMainAdapter (
    private val recipeList: ArrayList<RecipeModel>) :  Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_recipe_card, parent, false)
        Log.d("ReusableRecyclerView", "Recipe list size: ${recipeList.size}")
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipeList[position]
        holder.recipeNameTv.text = recipe.name
        Log.d("RecipeMainAdapter", "Binding recipe: ${recipe.name}")

//        holder.editButton.setOnClickListener { onEditClick(recipe) }
//        holder.deleteButton.setOnClickListener { onDeleteClick(recipe) }

        // TODO:Edit Button
        holder.editButton.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Edit clicked: ${recipe.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // TODO: Delete Button
        holder.deleteButton.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Delete clicked: ${recipe.name}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun getItemCount(): Int = recipeList.size
}