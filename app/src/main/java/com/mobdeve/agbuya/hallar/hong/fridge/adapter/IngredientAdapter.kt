package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class RecipeIngredientAdapter(
    private val ingredientList: List<RecipeModel.RecipeIngredient>
) : RecyclerView.Adapter<RecipeIngredientAdapter.IngredientViewHolder>() {

    class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTv: TextView = view.findViewById(R.id.ingredientNameTv)
        val amountTv: TextView = view.findViewById(R.id.ingredientAmountTv)  // Change to TextView
        val unitTv: TextView = view.findViewById(R.id.ingredientUnitTv)      // Change to TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_ingredient, parent, false)
        return IngredientViewHolder(view)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredientList[position]
        holder.nameTv.text = ingredient.name
        holder.amountTv.text = ingredient.amount.toString()
        holder.unitTv.text = ingredient.unit.displayName
    }

    override fun getItemCount(): Int = ingredientList.size
}
