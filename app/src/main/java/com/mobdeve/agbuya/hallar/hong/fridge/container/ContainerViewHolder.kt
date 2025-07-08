package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding

class ContainerViewHolder(private val binding: ContainerComponentBinding): RecyclerView.ViewHolder(binding.root)  {
    fun bindData(cont: Container){

        binding.containerIv.setImageResource(cont.image.getResId())
        binding.fridgeNameTv.text = cont.name
        binding.fridgeCapacityTv.text = binding.root.context.getString(R.string.capacity_format, cont.currCap, cont.maxCap)
        binding.fridgeTimeStampTv.text = cont.timeStamp


    }

}