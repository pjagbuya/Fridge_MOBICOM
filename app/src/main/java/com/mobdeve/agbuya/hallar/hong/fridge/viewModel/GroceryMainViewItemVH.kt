package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding

class GroceryMainViewItemVH(private val binding: GroceriesComponentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: Ingredient) {

        binding.containerIv.setImageResource(item.icon)
        binding.groceryTitleTv.setText(item.name.toString())
        val details: String ="Quantity: ${item.quantity} ${item.unit}\n" +
                "Date bought: ${item.dateAdded}\n" +
                "Expiration Date: ${item.expirationDate}\n" +
                "Stored in: DEFAULT CONTAINER"

        binding.groceryDetailsTv.setText(details)
    }
}
