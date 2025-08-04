package com.mobdeve.agbuya.hallar.hong.fridge.adapter

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerComponentEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.ColorPickerDialogFragment
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.ContainerActivityEditHolder
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel

class ContainerActivityAddAdapter(
    private val data: ArrayList<ContainerEntity>,
    private val activity: FragmentActivity,
    private val listener: ContainerEditActionListener,
    private val getContainerName: () -> String
) : RecyclerView.Adapter<ContainerActivityEditHolder>() {
    private val sharedContainerViewModel = ViewModelProvider(activity).get(ContainerSharedViewModel::class.java)
    private val userViewModel = ViewModelProvider(activity).get(UserViewModel::class.java) // Add UserViewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContainerActivityEditHolder {
        val binding =
            ContainerComponentEditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContainerActivityEditHolder(binding)
    }

    override fun onBindViewHolder(holder: ContainerActivityEditHolder, position: Int) {
        val model = data[position]
        holder.bindData(model)


        // TODO: Test Container Saving mechanisms
        holder.okBtn.setOnClickListener {
            val model = insertDataToDatabase(position)
            if(model!= null){
                listener.onOkClick(position, model)

            }
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


    private fun insertDataToDatabase(position: Int) : ContainerEntity?{
        val containerName = getContainerName()
        val model = data[position]

        if (!TextUtils.isEmpty(containerName)) {
            val imageContainer = ImageContainer(
                resId = model.imageContainer.getResId(),
                colorId = model.imageContainer.getColorId()
            )
            val containerEntity = ContainerEntity(
                name = containerName,
                imageContainer = imageContainer,
                currCap = 0,
                maxCap = 30,
                timeStamp = ContainerModel.getTimeStamp(),
                ownerUserId = userViewModel.loggedInUser.value?.id!! // Use actual logged-in user ID
            )

//            sharedContainerViewModel.addContainer(containerEntity, require)

            // Get current user ID from UserViewModel

            Toast.makeText(activity, "Successfully created Container", Toast.LENGTH_LONG).show()

            return containerEntity

        } else {
            Toast.makeText(activity, "ERROR: Please fill in an appropriate name", Toast.LENGTH_LONG).show()
        }

        return  null
    }


    override fun getItemCount(): Int {
        return data.size
    }

}