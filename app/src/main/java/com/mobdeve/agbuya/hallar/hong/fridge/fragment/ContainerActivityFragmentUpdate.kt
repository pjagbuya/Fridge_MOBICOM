package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.ContainerActivityEditAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.container.ContainerDataHelper.Companion.containerModelsSelection
import com.mobdeve.agbuya.hallar.hong.fridge.customInterface.ContainerEditActionListener
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ContainerActivityUpdateBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
class ContainerActivityFragmentUpdate : Fragment(){

    private val args by navArgs<ContainerActivityFragmentUpdateArgs>()

    private var containerName: String = ""
    companion object{
        val CONTAINERS_KEY : String = "CONTAINER_DATA_KEY"
        val CONTAINER_EDIT_NAME_KEY: String  = "CONTAINER_EDIT_NAME_KEY"
        val CONTAINER_ISCANCELED: String  = "CONTAINER_ISCANCELED"
        val ADD_RESULT : String = "ADD_RESULT"
        val EDIT_RESULT : String = "EDIT_RESULT"
    }
    private var _binding:ContainerActivityUpdateBinding? = null
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
        _binding = ContainerActivityUpdateBinding.inflate(inflater, container, false)
        val view = binding.root


        containerList= ContainerDataHelper.Companion.containerModelsSelection


        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopBar()
        setupRecycler()
        binding.containerNameEt.setText(args.currentContainer.name)
        containerName = args.currentContainer.name
        binding.containerNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                containerName = s.toString()
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
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
        binding.baseContainerTitleTopBar.headerTitleTv.setText(R.string.edit_container)
        isEdit = true
    }

    // TODO: Might need to segregate different adapters for different types of  editing ie. add or edit.
    private fun setupRecycler() {


        // Find which type of container user has selected
        val targetResId = args.currentContainer.imageContainer.getResId()
        val targetIndex = containerModelsSelection.indexOfFirst {
            it.imageContainer.getResId() == targetResId
        }


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


        // Setup snap helper and adapter data needed

        binding.containerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ContainerActivityEditAdapter(
                containerList,
                requireActivity(),
                object : ContainerEditActionListener {
                    override fun onOkClick(position: Int) {

                        findNavController().navigateUp()
                    }

                    override fun onCancelClick(position: Int) {

                        findNavController().navigateUp()
                    }
                },
                getContainerName = { containerName }, // 👈 live reference
                targetIndex,
                args.currentContainer
            )

        }

        // Make the snapHelper in the recyclerView scroll to position already
        if (targetIndex != -1) {
            binding.containerRecyclerView.scrollToPosition(targetIndex)
            binding.containerRecyclerView.post {
                val layoutManager = binding.containerRecyclerView.layoutManager as LinearLayoutManager
                val view = layoutManager.findViewByPosition(targetIndex)
                view?.let {
                    snapHelper.findSnapView(layoutManager)?.let { snapView ->
                        // Optional: Manually trigger highlighting logic here if needed
                    }
                }
            }
        }

    }

}