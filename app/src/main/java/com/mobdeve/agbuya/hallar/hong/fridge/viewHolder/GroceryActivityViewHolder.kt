package com.mobdeve.agbuya.hallar.hong.fridge.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding

class GroceryActivityViewHolder(private val binding: GroceriesComponentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: Ingredient) {
        item.icon.loadImageView(binding.containerIv)
        binding.groceryTitleTv.setText(item.name.toString())
        val details: String ="Quantity: ${item.amount} ${item.unit}\n" +
                "Date bought: ${item.dateAdded}\n" +
                "Expiration Date: ${item.expirationDate}\n" +
                "Stored in: DEFAULT CONTAINER"

        binding.groceryDetailsTv.setText(details)
    }
}
