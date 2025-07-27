package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ColorPickerDialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerActivityEditHolder(private val binding: ContainerComponentEditBinding): RecyclerView.ViewHolder(binding.root)  {

     lateinit var okBtn : Button
     lateinit var cancelBtn : Button
     lateinit var containerIv : ImageView
    fun bindData(cont: ContainerModel, selectedPosition: Int, currPos:Int){
        if (selectedPosition == -1)
            bindData(cont)
        else{
            containerIv = binding.containerIv
            okBtn = binding.okBtn
            cancelBtn = binding.cancelBtn
            if(selectedPosition == currPos){
                cont.imageContainer.loadImageView(binding.containerIv)

            }else{

            }

        }



    }
    fun bindData(cont: ContainerModel){
        containerIv = binding.containerIv
        cont.imageContainer.loadImageView(binding.containerIv)
        okBtn = binding.okBtn
        cancelBtn = binding.cancelBtn


    }

}