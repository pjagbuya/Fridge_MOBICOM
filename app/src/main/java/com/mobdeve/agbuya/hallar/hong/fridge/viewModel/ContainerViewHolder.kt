package com.mobdeve.agbuya.hallar.hong.fridge.viewModel

import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity

class ContainerViewHolder(private val binding: ContainerComponentBinding): RecyclerView.ViewHolder(binding.root)  {

    val editBtn : Button = binding.editBtn
    private val fridgeNameTv : TextView = binding.fridgeNameTv
    private val fridgeCapacityTv : TextView = binding.fridgeCapacityTv
    private val fridgeTimeStamp: TextView = binding.fridgeTimeStampTv
    fun bindData(cont: ContainerEntity){

        cont.imageContainer.loadImageView(binding.containerIv)
        fridgeNameTv.text = cont.name
        fridgeCapacityTv.text = binding.root.context.getString(R.string.capacity_format, cont.currCap, cont.maxCap)
        fridgeTimeStamp.text = cont.timeStamp


    }
    fun bindData(cont: ContainerModel){

        cont.imageContainer.loadImageView(binding.containerIv)
        fridgeNameTv.text = cont.name
        fridgeCapacityTv.text = binding.root.context.getString(R.string.capacity_format, cont.currCap, cont.maxCap)
        fridgeTimeStamp.text = cont.timeStamp


    }

}