package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.DialogImageZoomBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ImageLayoutViewBinding

class GroceryViewImageGridAdapterDefault(
    private val images: List<ImageRaw>,
    private val isEditable: Boolean
) : RecyclerView.Adapter<GroceryViewImageGridAdapterDefault.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ImageLayoutViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: ImageRaw, pos : Int) {
            binding.imageTemplate.setImageBitmap(item.getBitmap())

            binding.imageDeleteBtn.visibility = View.GONE
            binding.imageDeleteBtn.isEnabled = false

            binding.imageTemplate.setOnClickListener {
                val context = binding.root.context
                val dialog = android.app.Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

                // Inflate the dialog layout using ViewBinding
                val dialogBinding = DialogImageZoomBinding.inflate(LayoutInflater.from(context))
                val bitmap = item.getBitmap()
                if (bitmap != null) {
                    // Set image
                    Glide.with(context)
                        .asBitmap()
                        .load(item.getBlob()) // Assuming you have a getBytes() or just access `item.bytes`
                        .into(dialogBinding.zoomImageView)
                } else {
                    Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show()
                }

                // Show/hide remove button
                dialogBinding.removeBtn.visibility = if (isEditable) View.VISIBLE else View.GONE

                // Optional: Add click listener to remove or update the image
                // dialogBinding.removeBtn.setOnClickListener { /* your logic here */ }

                // Dismiss dialog on tap
                dialogBinding.zoomImageView.setOnClickListener { dialog.dismiss() }

                dialog.setContentView(dialogBinding.root)
                dialog.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageLayoutViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bindData(images[position], position)
    }

    override fun getItemCount(): Int = images.size
}