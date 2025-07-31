package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.BaseSearchbarContainerBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceriesActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentViewBinding
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.ADD_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.EDIT_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.fragment.GroceryActivityFragmentMain.Companion.SELECTED_INGREDIENT_KEY
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import okhttp3.internal.toImmutableList
import kotlin.getValue

class GroceryActivityFragmentView : Fragment() {

    private val args by navArgs<GroceryActivityFragmentViewArgs>()
    private var _binding: GroceryComponentViewBinding? = null
    private val binding get() = _binding!!
    private lateinit var groceryViewModel : GrocerySharedViewModel
    private lateinit var containerViewModel : ContainerSharedViewModel
    private lateinit var groceryList: ArrayList<Ingredient>
    private lateinit var selectedIngredient : Ingredient

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
        groceryViewModel = ViewModelProvider(this)[GrocerySharedViewModel::class.java]
        containerViewModel = ViewModelProvider(this)[ContainerSharedViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupIngredientView()
        setupRecycler()

        // TODO: setup button edit
        binding.updateBtn.setOnClickListener {
            val action = GroceryActivityFragmentViewDirections.actionGroceriesViewToGroceriesEdit(args.currGrocery)
            findNavController().navigate(action)
        }


        // TODO: setup button delete

        binding.deleteBtn.setOnClickListener {
            containerViewModel.decreaseCurrCap(args.currGrocery.attachedContainerId)
            groceryViewModel.deleteGrocery(args.currGrocery.ingredientID)
            findNavController().navigateUp()

        }


    }

    private fun setupIngredientView(){
        args.currGrocery.let {
            binding.groceryTitleTv.text = it.name
            binding.quantityLabelTv.text = "Quantity: ${it.quantity} ${it.unit}"
            binding.dateBoughtTv.text = "Date bought: ${it.dateAdded}"
            binding.expirationDateLabelTv.text = "Expiration date: ${it.expirationDate}"
            binding.storedInLabelTv.text = "Stored in: Container id ${it.attachedContainerId}"
            binding.categoryLabelTv.text = "Category: ${it.itemType}"
            binding.conditionLabelTv.text = "Condition: ${it.conditionType}"
            if(it.iconResId == -1){
                binding.imageView.setImageResource(R.mipmap.ic_itemtype_other)

            }else{
                binding.imageView.setImageResource(it.iconResId)

            }
        }
    }
    private fun setupRecycler() {

        val isEditable = arguments?.getBoolean(GroceryActivityFragmentMain.EDIT_INGREDIENT_KEY)!!

        val groceryCopy = args.currGrocery.copy(
            imageList = args.currGrocery.imageList
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
