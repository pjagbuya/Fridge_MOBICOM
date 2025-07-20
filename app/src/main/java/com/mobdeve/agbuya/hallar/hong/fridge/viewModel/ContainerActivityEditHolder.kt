package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import android.widget.Button
import android.widget.EditText
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