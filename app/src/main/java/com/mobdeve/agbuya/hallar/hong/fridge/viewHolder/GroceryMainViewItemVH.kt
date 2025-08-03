package com.mobdeve.agbuya.hallar.hong.fridge.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientEntity

class GroceryMainViewItemVH(private val binding: GroceriesComponentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(item: IngredientEntity) {
        if(item.iconResId == -1){
            binding.containerIv.setImageResource(R.mipmap.ic_itemtype_other)

        }else{
            binding.containerIv.setImageResource(item.iconResId)

        }

        binding.groceryTitleTv.setText(item.name.toString())
        val details: String ="Quantity: ${item.quantity} ${item.unit}\n" +
                "Date bought: ${item.dateAdded}\n" +
                "Expiration Date: ${item.expirationDate}\n" +
                "Stored in: Container id ${item.attachedContainerId}"

        binding.groceryDetailsTv.setText(details)
    }
}
