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
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentUpdateBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentViewBinding

class GroceryActivityFragmentEdit: Fragment() {

    private var _binding: GroceryComponentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var groceryList: ArrayList<Ingredient>
    private lateinit var selectedIngredient: Ingredient

    inline fun <reified T : Parcelable> Intent.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
        }

    inline fun <reified T : Parcelable> Bundle.parcelableArrayList(key: String): ArrayList<T>? =
        when {
            SDK_INT >= 33 -> getParcelableArrayList(key, T::class.java)
            else -> @Suppress("DEPRECATION") getParcelableArrayList(key)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroceryComponentUpdateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupIngredientView()
        setupDropdowns()
        setupRecycler()
        setupConditionRadios()


    }

    private fun setupIngredientView() {
        selectedIngredient =
            arguments?.getParcelable<Ingredient>(GroceryActivityFragmentMain.SELECTED_INGREDIENT_KEY)!!
        selectedIngredient.let {
            binding.itemNameEt.setText(it.name)
            binding.itemNumberEt.setText(it.quantity.toString())
            binding.unitTypeDropDownActv.setText(it.unit)
            binding.itemTypeDropDownActv.setText(it.itemType)
            binding.priceEt.setText("Php ${it.price}")
            binding.dateBoughtEt.setText(it.dateAdded)
            binding.expirationDateEt.setText(it.expirationDate)

            // TODO: must be able to have Container's id or name
            binding.containerTypeDropDownActv.setText("DEFAULT CONTAINER")

            when (it.conditionType.lowercase()) {
                "very ok" -> binding.radioVeryOk.isChecked = true
                "still ok" -> binding.radioStillOk.isChecked = true
                "slightly not ok" -> binding.radioSlightlyNotOk.isChecked = true
                "not ok" -> binding.radioNotOk.isChecked = true
            }
            binding.iconImageIv.setImageBitmap(it.icon.getBitmap())

        }
    }
    private fun setupConditionRadios() {
        val allButtons = listOf(
            binding.radioVeryOk,
            binding.radioStillOk,
            binding.radioSlightlyNotOk,
            binding.radioNotOk
        )

        for (button in allButtons) {
            button.setOnClickListener {
                allButtons.forEach { it.isChecked = false }
                button.isChecked = true
            }
        }
    }
    private fun setupDropdowns(){
        val layoutChosen = R.layout.dropdown_item_for_update
        var sortOptions = resources.getStringArray(R.array.item_type_array)
        var sortAdapter =
            ArrayAdapter(requireContext(), layoutChosen, sortOptions)
        binding.itemTypeDropDownActv.setAdapter(sortAdapter)


        sortOptions = resources.getStringArray(R.array.unit_types)
        sortAdapter =
            ArrayAdapter(requireContext(), layoutChosen, sortOptions)
        binding.unitTypeDropDownActv.setAdapter(sortAdapter)


        // TODO: Adapter filled with container selection of fridges available
//        sortOptions = resources.getStringArray(R.array.unit_types) //must return contianer array here
//        sortAdapter =
//            ArrayAdapter(requireContext(), R.layout.dropdown_item, sortOptions)
//        binding.containerTypeDropDownActv.setAdapter(sortAdapter)
    }
    private fun setupRecycler() {

        val isEditable = arguments?.getBoolean(GroceryActivityFragmentMain.EDIT_INGREDIENT_KEY)!!

        selectedIngredient.let {
            binding.imagesRecyclerViewRv.adapter =
                GroceryViewImageGridAdapter(it.imageContainerLists, isEditable)
        }
        binding.imagesRecyclerViewRv.layoutManager = GridLayoutManager(requireContext(), 3)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}