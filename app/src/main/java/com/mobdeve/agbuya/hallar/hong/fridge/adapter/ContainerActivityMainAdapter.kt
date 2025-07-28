package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewHolder.ContainerViewHolder
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ContainerActivityFragmentMainDirections
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ContainerActivityFragmentUpdateDirections
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity

class ContainerActivityMainAdapter(
    private val onClick: () -> Unit
) : RecyclerView.Adapter<ContainerViewHolder>() {
    private var containerData: List<ContainerEntity> = emptyList<ContainerEntity>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ContainerViewHolder {
        val binding = ContainerComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        val currCont = containerData[position]
        holder.bindData(currCont)

        holder.editBtn.setOnClickListener {
            val action = ContainerActivityFragmentMainDirections.actionContainerMainToContainerUpdate(currCont)
            holder.itemView.findNavController().navigate(action)
            onClick()
        }

    }


    override fun getItemCount(): Int {
        return containerData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(containers : List<ContainerEntity>){
        this.containerData = containers
        notifyDataSetChanged()

    }

}