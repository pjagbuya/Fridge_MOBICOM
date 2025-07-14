package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerViewHolder
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding

class ContainerActivityAdapter(
    private val data: ArrayList<ContainerModel>,
    private val onClick: () -> Unit
) : RecyclerView.Adapter<ContainerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ContainerViewHolder {
        val binding = ContainerComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {

        holder.bindData(data[position])

        holder.editBtn.setOnClickListener {
            onClick()
        }

    }


    override fun getItemCount(): Int {
        return data.size
    }

}