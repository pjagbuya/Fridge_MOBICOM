package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ColorPickerDialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.ContainerSharedViewModel


class ContainerActivityEditAdapter(
    private val data: ArrayList<ContainerModel>,
    private val activity: FragmentActivity,
    private val listener: ContainerEditActionListener,
    private val getContainerName: () -> String,
    private val selectedPosition: Int,
    private val selectedColorId : Int
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {
    val sharedContainerViewModel = ViewModelProvider(activity).get(ContainerSharedViewModel::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {
        val model = data[position]
        holder.bindData(model, selectedPosition, position)

        // Color choice picker
        // Given a presence of selected position
        if (position == selectedPosition) {
            // Highlight color using container.imageContainer.getColorId()
            holder.containerIv.setColorFilter(selectedColorId)
        } else {
            // Random Color selected
            holder.containerIv.setColorFilter(model.imageContainer.getColorId())
        }

        // TODO: Test Container Saving mechanisms
        holder.okBtn.setOnClickListener {
            insertDataToDatabase(position)
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

    private fun updateDataToDatabase(position : Int){
        val containerName = getContainerName()
        val model = data[position]

        if (!TextUtils.isEmpty(containerName)) {
            val imageContainer = ImageContainer(
                resId = model.imageContainer.getResId(),
                colorId = model.imageContainer.getColorId()
            )

            // TODO:  Make ownerUserId of this entity be attached to a user that is in session. currently the entity is defaulting to user 0
            val containerEntity = ContainerEntity(
                name = containerName,
                imageContainer = imageContainer,
                currCap = 0,
                maxCap = 30,
                timeStamp = ContainerModel.getTimeStamp(),
                ownerUserId = 1
            )
            sharedContainerViewModel.addContainer(containerEntity)
            Toast.makeText(activity, "Successfully created Container", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(activity, "ERROR: Please fill in an appropriate name", Toast.LENGTH_LONG).show()
        }
    }

    private fun insertDataToDatabase(position : Int){

        val containerName = getContainerName()
        val model = data[position]

        if (!TextUtils.isEmpty(containerName)) {
            val imageContainer = ImageContainer(
                resId = model.imageContainer.getResId(),
                colorId = model.imageContainer.getColorId()
            )

            // TODO:  Make ownerUserId of this entity be attached to a user that is in session. currently the entity is defaulting to user 0
            val containerEntity = ContainerEntity(
                name = containerName,
                imageContainer = imageContainer,
                currCap = 0,
                maxCap = 30,
                timeStamp = ContainerModel.getTimeStamp(),
                ownerUserId = 1
            )

            sharedContainerViewModel.addContainer(containerEntity)
            Toast.makeText(activity, "Successfully created Container", Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(activity, "ERROR: Please fill in an appropriate name", Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}