package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityEditAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityEditBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
enum class EditType{
    ADD,
    EDIT
}
class ContainerActivityFragmentEdit : Fragment(){
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"
        val CONTAINER_EDIT_NAME_KEY: String  = "CONTAINER_EDIT_NAME_KEY"
        val CONTAINER_ISCANCELED: String  = "CONTAINER_ISCANCELED"
        val ADD_RESULT : String = "ADD_RESULT"
        val EDIT_RESULT : String = "EDIT_RESULT"
    }
    private var _binding:ContainerActivityEditBinding? = null
    private var editType: Int = -1
    private var isEdit: Boolean = false
    private val binding get() = _binding!!

    private lateinit var containerList:ArrayList<ContainerModel>


    // These two inlines suppresses deprecation errors
    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }


    fun getcontainerList():ArrayList<ContainerModel>{
        return containerList
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        _binding = ContainerActivityEditBinding.inflate(inflater, container, false)
        val view = binding.root


        containerList= ContainerDataHelper.Companion.containerModelsSelection


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupRecycler()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(CONTAINERS_KEY, containerList)

    }
    private fun setupTopBar() {
        val activity = activity ?: return


        editType = arguments?.getInt("EDIT_TYPE", -1) ?: -1
        if(editType == EditType.ADD.ordinal){
            binding.baseContainerTitleTopBar.headerTitleTv.setText(R.string.add_container)
            isEdit = false
        }else{
            binding.baseContainerTitleTopBar.headerTitleTv.setText(R.string.edit_container)
            isEdit = true
        }
    }

    private fun setupRecycler() {

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
        snapHelper.attachToRecyclerView(binding.containerRecyclerView)



        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter =
                ContainerActivityEditAdapter(containerList, requireActivity(), object : ContainerEditActionListener {
                    override fun onOkClick(position: Int) {
                        val result = Bundle().apply {
                            // TODO: Finish fragments to also pass color of container based on adapter position
                            putString(CONTAINER_EDIT_NAME_KEY, "Fruits")
                            putBoolean(CONTAINER_ISCANCELED, false)
                        }

                        if(isEdit){
                            parentFragmentManager.setFragmentResult(EDIT_RESULT, result)

                        }else{
                            parentFragmentManager.setFragmentResult(ADD_RESULT, result)

                        }
                        findNavController().navigateUp() // goes back to previous fragment
                    }

                    override fun onCancelClick(position: Int) {
                        val result = Bundle().apply {
                            // TODO: Finish fragments to also pass color of container based on adapter position
                            putString(CONTAINER_EDIT_NAME_KEY, "Fruits")
                            putBoolean(CONTAINER_ISCANCELED, true)
                        }
                        if(isEdit){
                            parentFragmentManager.setFragmentResult(EDIT_RESULT, result)

                        }else{
                            parentFragmentManager.setFragmentResult(ADD_RESULT, result)

                        }
                        findNavController().navigateUp()
                    }
                })
        }

    }

}