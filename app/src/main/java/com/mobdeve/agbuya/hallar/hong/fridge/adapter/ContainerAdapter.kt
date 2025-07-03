package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ViewholderContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import java.text.SimpleDateFormat
import java.util.*

class ContainerAdapter(private val data: MutableList<ContainerModel>) :
    RecyclerView.Adapter<ContainerAdapter.Viewholder>() {

    inner class Viewholder(val binding: ViewholderContainerBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Viewholder {
        val binding = ViewholderContainerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        val container = data[position]
        holder.binding.containerNameTv.text = container.containerName
        holder.binding.capacityTv.text = container.capacity

        // formatting date and time
        val rawDate = container.lastUpdated
        val inputFormat = SimpleDateFormat("yyyy-MM-dd|HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault())

        val formattedDate = try {
            val parsedDate = rawDate?.let { inputFormat.parse(it) }
            parsedDate?.let { outputFormat.format(it) } ?: "No date"
        } catch (e: Exception) {
            "Invalid date"
        }

        holder.binding.lastUpdatedTv.text = "Last Updated: $formattedDate"
    }

    override fun getItemCount(): Int = data.size
}
