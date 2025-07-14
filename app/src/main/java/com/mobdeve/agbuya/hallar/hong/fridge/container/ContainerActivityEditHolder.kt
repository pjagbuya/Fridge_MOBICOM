package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerActivityEditHolder(private val binding: ContainerComponentEditBinding): RecyclerView.ViewHolder(binding.root)  {

     lateinit var okBtn : Button
     lateinit var cancelBtn : Button
     lateinit var containerNameEt : EditText
    fun bindData(cont: ContainerModel){

        cont.imageContainer.loadImageView(binding.containerIv)
        binding.containerNameEt.setText(cont.name)
        containerNameEt = binding.containerNameEt
        okBtn = binding.okBtn
        cancelBtn = binding.cancelBtn




    }

}