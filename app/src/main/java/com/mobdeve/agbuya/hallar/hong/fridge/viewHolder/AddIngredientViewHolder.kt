package com.mobdeve.agbuya.hallar.hong.fridge.viewHolder

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R

class AddIngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ingredientNameTv: TextView = itemView.findViewById(R.id.addIngredientNameTv)
    val addIngredientBtn: Button = itemView.findViewById(R.id.addIngredientBtn)
}