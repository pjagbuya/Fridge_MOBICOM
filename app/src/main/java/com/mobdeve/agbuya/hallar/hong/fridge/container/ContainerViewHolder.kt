package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding

class ContainerViewHolder(private val binding: ContainerComponentBinding): RecyclerView.ViewHolder(binding.root)  {

    val editBtn : Button = binding.editBtn
    val deleteBtn : Button = binding.deleteBtn

    fun bindData(cont: ContainerModel){

        cont.imageContainer.loadImageView(binding.containerIv)
        binding.fridgeNameTv.text = cont.name
        binding.fridgeCapacityTv.text = binding.root.context.getString(R.string.capacity_format, cont.currCap, cont.maxCap)
        binding.fridgeTimeStampTv.text = cont.timeStamp


    }

}