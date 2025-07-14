package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerViewHolder
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class ContainerActivityEditAdapter(
    private val data: ArrayList<ContainerModel>,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {

        holder.bindData(data[position])

        holder.okBtn.setOnClickListener {
            onClick()
        }


    }


    override fun getItemCount(): Int {
        return data.size
    }

}