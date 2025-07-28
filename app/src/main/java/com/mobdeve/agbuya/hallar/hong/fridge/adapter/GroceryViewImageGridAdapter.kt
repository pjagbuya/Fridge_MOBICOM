package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ImageLayoutViewBinding

class GroceryViewImageGridAdapter(
    private val images: List<ImageRaw>,
    private val isEditable: Boolean
) : RecyclerView.Adapter<GroceryViewImageGridAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ImageLayoutViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(item: ImageRaw) {
            binding.imageTemplate.setImageBitmap(item.getBitmap())
            if(isEditable){
                binding.imageDeleteBtn.visibility = View.VISIBLE

            }else{
                binding.imageDeleteBtn.visibility = View.GONE
            }
            binding.imageTemplate.setOnClickListener {
                val dialog = android.app.Dialog(binding.root.context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                val view = LayoutInflater.from(binding.root.context).inflate(R.layout.dialog_image_zoom, null)
                val zoomImageView = view.findViewById<ImageView>(R.id.zoomImageView)
                val removeBtn = view.findViewById<ImageView>(R.id.removeBtn)
                if(isEditable){
                    removeBtn.visibility = View.VISIBLE

                }else{
                    removeBtn.visibility = View.GONE
                }
                // TODO:: Make onClick listener for button to update image.
                // removeBtn.setOnClickListener

                zoomImageView.setImageBitmap(item.getBitmap())
                dialog.setContentView(view)
                zoomImageView.setOnClickListener { dialog.dismiss() } // Dismiss on tap
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
        holder.bindData(images[position])
    }

    override fun getItemCount(): Int = images.size
}