package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.GroceryMainViewItemVH

class GroceryActivityMainAdapter(
    private val data: ArrayList<Ingredient>,
    private val onClick: (Ingredient) -> Unit
) : RecyclerView.Adapter<GroceryMainViewItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryMainViewItemVH {
        val binding = GroceriesComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryMainViewItemVH(binding)
    }

    override fun onBindViewHolder(holder: GroceryMainViewItemVH, position: Int) {
        holder.bindData(data[position])

        holder.itemView.setOnClickListener {
            onClick(data[position]) // <- send clicked item
        }
    }

    override fun getItemCount(): Int = data.size
}
