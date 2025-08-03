package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMainDirections
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.viewHolder.GroceryMainViewItemVH

class GroceryActivityMainAdapter(groceryList: MutableList<Ingredient>, function: () -> Unit) : RecyclerView.Adapter<GroceryMainViewItemVH>() {
    private var groceriesData: List<IngredientEntity> = emptyList<IngredientEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryMainViewItemVH {
        val binding = GroceriesComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryMainViewItemVH(binding)
    }

    override fun onBindViewHolder(holder: GroceryMainViewItemVH, position: Int) {
        val currGrocery = groceriesData[position]
        holder.bindData(currGrocery)

        holder.itemView.setOnClickListener {
            val action = GroceryActivityFragmentMainDirections.actionGroceriesMainToGroceriesView(currGrocery)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int = groceriesData.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(groceries : List<IngredientEntity>){
        this.groceriesData = groceries
        notifyDataSetChanged()

    }

}
