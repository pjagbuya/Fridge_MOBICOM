package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.GroceryActivityViewHolder

class GroceryActivityMainAdapter(
    private val data: ArrayList<Ingredient>
) : RecyclerView.Adapter<GroceryActivityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryActivityViewHolder {
        val binding = GroceriesComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryActivityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryActivityViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int = data.size
}
