package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.viewHolder.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel


class ContainerActivityEditAdapter(
    private val data: ArrayList<ContainerModel>,
    private val listener: ContainerEditActionListener
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {

        holder.bindData(data[position])

        holder.okBtn.setOnClickListener {
            listener.onOkClick(position)
        }

        holder.cancelBtn.setOnClickListener {
            listener.onCancelClick(position)
        }




    }


    override fun getItemCount(): Int {
        return data.size
    }

}