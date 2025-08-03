package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ColorPickerDialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel

class ContainerActivityEditAdapter(
    private val data: ArrayList<ContainerEntity>,
    private val activity: FragmentActivity,
    private val listener: ContainerEditActionListener,
    private val getContainerName: () -> String,
    private val selectedPosition: Int,
    private val selectedContainer: ContainerEntity
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {

    // Use activityViewModels instead of ViewModelProvider for better Hilt integration
    private val sharedContainerViewModel: ContainerSharedViewModel by lazy {
        ViewModelProvider(activity)[ContainerSharedViewModel::class.java]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {
        val model = data[position]

        if (position == selectedPosition) {
            model.imageContainer.setColorId(selectedContainer.imageContainer.getColorId())
        } else {
            model.imageContainer.setColorId(model.imageContainer.getColorId())
        }

        holder.bindData(model, selectedPosition, position)

        holder.okBtn.setOnClickListener {

            val model = updateDataToDatabase(position)
            listener.onOkClick(position, model)
        }

        holder.cancelBtn.setOnClickListener {
            listener.onCancelClick(position)
        }

        holder.containerIv.setOnClickListener {
            ColorPickerDialogFragment { selectedColor ->
                model.imageContainer.setColorId(selectedColor)
                model.imageContainer.loadImageView(holder.containerIv)
            }.show(activity.supportFragmentManager, "colorPickerDialog")
        }
    }

    private fun updateDataToDatabase(position: Int): ContainerEntity {
        val containerName = getContainerName()
        val model = data[position]

        if (!TextUtils.isEmpty(containerName)) {
            val containerEntity = ContainerEntity(
                containerId = selectedContainer.containerId,
                name = containerName,
                imageContainer = model.imageContainer,
                currCap = selectedContainer.currCap,
                maxCap = selectedContainer.maxCap,
                timeStamp = ContainerModel.getTimeStamp(),
                ownerUserId = selectedContainer.ownerUserId // This is now user-specific
            )

            sharedContainerViewModel.updateContainer(containerEntity)
            Toast.makeText(activity, "Successfully updated Container", Toast.LENGTH_LONG).show()
            return containerEntity
        } else {
            Toast.makeText(activity, "ERROR: Please fill in an appropriate name", Toast.LENGTH_LONG).show()
        }
        return model
    }

    override fun getItemCount(): Int {
        return data.size
    }
}