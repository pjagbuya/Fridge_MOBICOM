package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.R.id.container_activity_main_fragment
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityEditAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityEditFragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerActivityFragment
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

class PaulMainActivity : AppCompatActivity() {
    companion object{
        const val EDIT_TYPE_KEY = "EDIT_TYPE"

    }
    private lateinit var  activityMainBinding : ActivityMainBinding

    private lateinit var  containerModels: ArrayList<ContainerModel>

    private val newContainerResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Check to see if the result returned is appropriate (i.e. OK)
        if (result.resultCode == RESULT_OK) {
            // TODO get back the changes to the recycler view and correspondingly add it


        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        containerModels = ContainerDataHelper.initializeContainers(this@PaulMainActivity)
        setContentView(activityMainBinding.root)

        activityMainBinding.apply {

            // Get binding of the content to change
            val contentBinding = ContainerActivityMainBinding.inflate(layoutInflater)
            // Set view
            dynamicContent.addView(contentBinding.root)

            // Setup of Behaviors
            contentBinding.containerRecyclerView.adapter = ContainerActivityAdapter(containerModels){
                val intent: Intent = Intent(this@PaulMainActivity, ContainerActivityEdit::class.java)

                intent.putExtra(ContainerActivityEdit.EDIT_TYPE_KEY, EditType.EDIT.ordinal)

                newContainerResultLauncher.launch(intent)
            }
            contentBinding.addContainerBtn.setOnClickListener {
                val intent: Intent = Intent(this@PaulMainActivity, ContainerActivityEdit::class.java)

                intent.putExtra(ContainerActivityEdit.EDIT_TYPE_KEY, EditType.ADD.ordinal)

                newContainerResultLauncher.launch(intent)
            }
            contentBinding.containerRecyclerView.layoutManager = LinearLayoutManager(this@PaulMainActivity)


            topBar.removeAllViews()
            val topBarContent = BaseSearchbarContainerBinding.inflate(layoutInflater)
            topBar.addView(topBarContent.root)


            // TODO: Must be the name of fridge collection of the user ie. containerHeaderTv text = ???
            topBarContent.containerHeaderTv.setText(R.string.my_container)

            // TODO: Do the similar logic connection with the navigation bar to have new intents on other screen. Set behavior of the buttons
        }


    }



}