package com.mobdeve.agbuya.hallar.hong.fridge.viewHolder

import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerActivityEditHolder(private val binding: ContainerComponentEditBinding): RecyclerView.ViewHolder(binding.root)  {

     lateinit var okBtn : Button
     lateinit var cancelBtn : Button

    fun bindData(cont: ContainerModel){

        cont.imageContainer.loadImageView(binding.containerIv)
        okBtn = binding.okBtn
        cancelBtn = binding.cancelBtn
    }
}