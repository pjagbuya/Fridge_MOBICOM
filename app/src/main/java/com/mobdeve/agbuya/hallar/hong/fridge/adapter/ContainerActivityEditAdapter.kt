package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ColorPickerDialogFragment


class ContainerActivityEditAdapter(
    private val data: ArrayList<ContainerModel>,
    private val activity: FragmentActivity,
    private val listener: ContainerEditActionListener
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {
        val model = data[position]
        holder.bindData(model)

        holder.okBtn.setOnClickListener {
            listener.onOkClick(position)
        }

        holder.cancelBtn.setOnClickListener {
            listener.onCancelClick(position)
        }
        holder.containerIv.setOnClickListener {
            ColorPickerDialogFragment { selectedColor ->
                // Update model
                model.imageContainer.setColorId(selectedColor)
                model.imageContainer.loadImageView(holder.containerIv)
            }.show(activity.supportFragmentManager, "colorPickerDialog")

        }



    }


    override fun getItemCount(): Int {
        return data.size
    }

}