package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityEditAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel

enum class EditType {
    ADD,
    EDIT,
}
class ContainerActivityEdit()  : AppCompatActivity() {
    companion object{


        const val EDIT_TYPE_KEY = "EDIT_TYPE"

    }

    val c_TAG = "CONTAINER_ACTIVITY_E"
    private lateinit var  activityMainBinding: ActivityMainBinding
    private lateinit var  containerActivityEditBinding: ContainerActivityEditBinding
    private lateinit var containerSelectionModels : ArrayList<ContainerModel>

    private var focusedPosition = RecyclerView.NO_POSITION
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // Restart activityMainBinding
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        containerActivityEditBinding = ContainerActivityEditBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        containerSelectionModels = ContainerDataHelper.containerModelsSelection


        // TODO: Return RESULT CODE back to Main Activity Container
        containerActivityEditBinding.containerRecyclerView.adapter = ContainerActivityEditAdapter(containerSelectionModels, object :
            ContainerEditActionListener {
            override fun onOkClick(position: Int) {
                finish()
            }

            override fun onCancelClick(position: Int) {
                finish()
            }
        })



        val layoutManager = LinearLayoutManager(
            this@ContainerActivityEdit,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        containerActivityEditBinding.containerRecyclerView.layoutManager = layoutManager
        containerActivityEditBinding.containerNameEt.setText("My Fridge 1")


        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(
                layoutManager: RecyclerView.LayoutManager,
                velocityX: Int,
                velocityY: Int
            ): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }

                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))

                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(containerActivityEditBinding.containerRecyclerView)
        activityMainBinding.apply{

            // Set view
            dynamicContent.addView(containerActivityEditBinding.root)

            topBar.removeAllViews()
            val topBarContent = BaseContainerBinding.inflate(layoutInflater)
            topBar.addView(topBarContent.root)

            val editType = intent.getIntExtra(EDIT_TYPE_KEY, -1)
            if(editType != -1 && editType == EditType.ADD.ordinal){
                topBarContent.containerHeaderTv.setText(R.string.add_container)
            }else{
                topBarContent.containerHeaderTv.setText(R.string.edit_container)

            }

        }

    }



}