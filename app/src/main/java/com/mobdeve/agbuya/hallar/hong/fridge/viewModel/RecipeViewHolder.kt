package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R

class RecipeViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val recipeNameTv: TextView = itemView.findViewById(R.id.recipeNameTv)
    val editButton: Button = itemView.findViewById(R.id.recipeEditBt)
    val deleteButton: Button = itemView.findViewById(R.id.recipeDeleteBt)
}