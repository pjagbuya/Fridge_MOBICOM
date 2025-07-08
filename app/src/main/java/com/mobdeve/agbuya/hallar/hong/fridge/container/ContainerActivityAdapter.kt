package com.mobdeve.agbuya.hallar.hong.fridge.container

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Container
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentBinding

class ContainerActivityAdapter(private val data: ArrayList<Container>) : RecyclerView.Adapter<ContainerViewHolder>() {



    override fun onCreateViewHolder(parent:ViewGroup, viewType: Int) : ContainerViewHolder{
        val binding = ContainerComponentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    /*
     * You thought it was a comment, but no!! It's me, DIO!
     * */
    override fun getItemCount(): Int {
        return data.size
    }

}