package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.recyclerview.widget.GridLayoutManager
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentUpdateBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentViewBinding

class GroceryActivityFragmentEdit: Fragment() {

    private var _binding: GroceryComponentUpdateBinding? = null
    private val binding get() = _binding!!

    private lateinit var imagesList: ArrayList<ImageRaw>
    private lateinit var selectedIngredient: Ingredient

    // Camera setup
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>
    private var tempCameraImageUri: Uri? = null

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

        // Setup camera launcher states
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                selectedIngredient.imageContainerLists.add(ImageRaw(bitmap))
                binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(selectedIngredient.imageContainerLists.size - 1)
            }
        }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && tempCameraImageUri != null) {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        tempCameraImageUri
                    )
                    selectedIngredient.imageContainerLists.add(ImageRaw(bitmap))
                    binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(selectedIngredient.imageContainerLists.size - 1)
                }
            }

        val isAddable = arguments?.getBoolean(GroceryActivityFragmentMain.ADD_INGREDIENT_KEY)!!
        if (isAddable) {
            // create default/empty ingredient
            selectedIngredient = Ingredient(
                icon = ImageRaw(ImageRaw.getDefaultBitmap(requireContext())), // You must define a default/fallback icon in ImageRaw
                name = "",
                quantity = 0.0,
                price = 0.0,
                dateAdded = "", // or current date
                expirationDate = "",
                imageContainerLists = ArrayList<ImageRaw>()
            )
            binding.headerActionItemLabelTv.text = "Add Grocery"
        } else {

            selectedIngredient =
                arguments?.getParcelable(GroceryActivityFragmentMain.SELECTED_INGREDIENT_KEY)!!
            binding.headerActionItemLabelTv.text = "Update Item"
            selectedIngredient?.let { setupIngredientView(it) }
        }
        setupDropdowns()
        setupRecycler()
        setupConditionRadios()

        // Setup add image button
        binding.AddImageBtn.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.photo_method_choice_layout, null)

            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()

            dialogView.findViewById<Button>(R.id.gotoGalleryBtn).setOnClickListener {
                pickImageLauncher.launch("image/*")
                dialog.dismiss()
            }

            dialogView.findViewById<Button>(R.id.takePhotoBtn).setOnClickListener {
                tempCameraImageUri = createTempImageUri()
                tempCameraImageUri?.let { uri ->
                    takePhotoLauncher.launch(uri)
                }
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun createTempImageUri(): Uri {
        val resolver = requireContext().contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
    private fun setupIngredientView(ingredient: Ingredient) {
        binding.itemNameEt.setText(ingredient.name)
        binding.itemNumberEt.setText(ingredient.quantity.toString())
        binding.unitTypeDropDownActv.setText(ingredient.unit)
        binding.itemTypeDropDownActv.setText(ingredient.itemType)
        binding.priceEt.setText("Php ${ingredient.price}")
        binding.dateBoughtEt.setText(ingredient.dateAdded)
        binding.expirationDateEt.setText(ingredient.expirationDate)
        binding.containerTypeDropDownActv.setText("DEFAULT CONTAINER")

        when (ingredient.conditionType.lowercase()) {
            "very ok" -> binding.radioVeryOk.isChecked = true
            "still ok" -> binding.radioStillOk.isChecked = true
            "slightly not ok" -> binding.radioSlightlyNotOk.isChecked = true
            "not ok" -> binding.radioNotOk.isChecked = true
        }

        binding.iconImageIv.setImageBitmap(ingredient.icon.getBitmap())
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
        binding.imagesRecyclerViewRv.layoutManager = GridLayoutManager(requireContext(), 2)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}