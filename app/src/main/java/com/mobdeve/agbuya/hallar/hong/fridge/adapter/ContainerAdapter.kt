package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ItemContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.model.ContainerModel
import java.text.SimpleDateFormat
import java.util.*

class ContainerAdapter(private val data: List<ContainerModel>) :
    RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val container = data[position]
        holder.binding.containerNameTv.text = container.containerName
        holder.binding.containerCapacityTv.text = container.capacity

        // Date formatting
        val inputFormat = SimpleDateFormat("yyyy-MM-dd|HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())
        holder.binding.containerUpdatedTv.text = try {
            val date = inputFormat.parse(container.lastUpdated)
            "Last Updated: ${date?.let { outputFormat.format(it) }}"
        } catch (e: Exception) {
            "Invalid date"
        }
    }

    override fun getItemCount(): Int = data.size
}
