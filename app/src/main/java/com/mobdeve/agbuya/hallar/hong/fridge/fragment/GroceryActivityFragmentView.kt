package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryActivityMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapterDefault
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentViewBinding
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.ADD_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.EDIT_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.SELECTED_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableList
import kotlin.getValue

@AndroidEntryPoint
class GroceryActivityFragmentView : Fragment() {

    private val args by navArgs<GroceryActivityFragmentViewArgs>()
    private var _binding: GroceryComponentViewBinding? = null
    private val binding get() = _binding!!
    // Info for data models to modify
    private val containerViewModel: ContainerSharedViewModel by viewModels()
    private val groceryViewModel: GrocerySharedViewModel by viewModels()
    private lateinit var groceryList: ArrayList<Ingredient>
    private lateinit var idToNameMap : Map<Int, String>
    private lateinit var selectedIngredient : IngredientEntity

    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? = when {
        SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroceryComponentViewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groceryViewModel.findGrocery(args.currGrocery.ingredientID).collect { ingredient ->
                    selectedIngredient = ingredient
                    setupIngredientView()
                    setupRecycler()

                    binding.updateBtn.setOnClickListener {
                        val action = GroceryActivityFragmentViewDirections.actionGroceriesViewToGroceriesEdit(selectedIngredient)
                        findNavController().navigate(action)
                    }

                    binding.deleteBtn.setOnClickListener {
                        containerViewModel.decreaseCurrCap(selectedIngredient.attachedContainerId)
                        groceryViewModel.deleteGrocery(selectedIngredient.ingredientID)
                        val firestoreHelper = FirestoreHelper(requireContext())
                        groceryViewModel.syncDeletedIngredient(selectedIngredient.ingredientID, requireContext()) // Assuming it uses ID only now
                        lifecycleScope.launch {
                            try {
                                // Sync groceries - ONE TIME sync
                                val containers = containerViewModel.readAllData.value
                                containers.forEach { container ->
                                    val firestoreContainer = container.toFirestoreContainer()
                                    // Use grocery ID as document ID, not user ID
                                    firestoreHelper.syncToFirestore("containers", container.containerId.toString(), firestoreContainer)
                                }
                            } catch (e: Exception) {
                                // Handle error
                                Log.d("ERRORS in CONTAINERS","Conversion error: ${e.message}")

                            }
                        }
                        findNavController().navigateUp()
                    }
                }

            }
        }



    }

    private fun setupIngredientView(){
        selectedIngredient.let {
            binding.groceryTitleTv.text = it.name
            binding.quantityLabelTv.text = "Quantity: ${it.quantity} ${it.unit}"
            binding.dateBoughtTv.text = "Date bought: ${it.dateAdded}"
            binding.expirationDateLabelTv.text = "Expiration date: ${it.expirationDate}"
            binding.categoryLabelTv.text = "Category: ${it.itemType}"
            binding.conditionLabelTv.text = "Condition: ${it.conditionType}"
            if(it.iconResId == -1){
                binding.imageView.setImageResource(R.mipmap.ic_itemtype_other)

            }else{
                binding.imageView.setImageResource(it.iconResId)

            }

// ... inside onViewCreated or another appropriate lifecycle method ...
            lifecycleScope.launch {
                // Use repeatOnLifecycle to collect while the Fragment is in a STARTED state or higher
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    // Collect the Flow<List<ContainerEntity>> from the ViewModel
                    containerViewModel.readAllData.collect { containerList ->
                        // This block runs every time containerViewModel.readAllData emits a new list
                        Log.d("ContainerMaps", "Received updated container list with ${containerList.size} items")

                        // Build the maps from the latest list of containers
                        idToNameMap = containerList.associate { it.containerId to it.name }
                        binding.storedInLabelTv.text = "Stored in: Container ${idToNameMap[it.attachedContainerId]}"

                    }
                }
            }
        }
    }
    private fun setupRecycler() {

        val isEditable = arguments?.getBoolean(GroceryActivityFragmentMain.EDIT_INGREDIENT_KEY)!!

        val groceryCopy = selectedIngredient.copy(
            imageList = selectedIngredient.imageList
        )

        groceryCopy.let {
            binding.groceryDetailsRv.adapter = GroceryViewImageGridAdapterDefault(it.imageList.toImmutableList(), isEditable)
        }

        binding.groceryDetailsRv.layoutManager = GridLayoutManager(requireContext(), 2)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
