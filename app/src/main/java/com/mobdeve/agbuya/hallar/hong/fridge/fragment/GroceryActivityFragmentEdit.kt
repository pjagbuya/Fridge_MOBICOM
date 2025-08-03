package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.OpenFoodFactsResponse
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentUpdateBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentViewBinding
import com.mobdeve.agbuya.hallar.hong.fridge.retrofitCall.RetrofitInstanceOPF
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.OpenFoodFactsApi
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreContainer
import com.mobdeve.agbuya.hallar.hong.fridge.converters.toFirestoreIngredient
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerIdName
import com.mobdeve.agbuya.hallar.hong.fridge.firestoreHelper.FirestoreHelper
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
@AndroidEntryPoint

class GroceryActivityFragmentEdit: Fragment() {
    private val args by navArgs<GroceryActivityFragmentEditArgs>()
    private var _binding: GroceryComponentUpdateBinding? = null
    private var selectedIconId : Int = -1
    private var originalIconId : Int = -1
    private var selectedContainerId : Int = -1

    private val binding get() = _binding!!

    private lateinit var imagesList: ArrayList<ImageRaw>
    private lateinit var selectedIngredient: IngredientEntity


    // Camera setup
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>

    // QR setup
    private lateinit var barcodeLauncher: ActivityResultLauncher<Intent>
    private val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
    }

    // Info for data models to modify
    private val containerViewModel: ContainerSharedViewModel by viewModels()
    private val groceryViewModel: GrocerySharedViewModel by viewModels()


    private lateinit var idToNameMap : Map<Int, String>

    private var originalContainerId : Int = -1


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
        // Readying text inputs on current data
        setupConditionRadios()
        setupDropdowns()
        setupTakePhotoFromGalleryOrCamera()
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                groceryViewModel.findGrocery(args.currGrocery.ingredientID).collect { ingredient ->
                    selectedIngredient = ingredient
                    binding.headerActionItemLabelTv.text = "Update Item" // Set title
                    setupIngredientView(ingredient) // Setup views with data
                    originalContainerId = ingredient.attachedContainerId // Store original container ID
                    setupRecycler() // Setup recycler view
                    // Any other initialization that depends on the ingredient data
                }
            }
        }

        // TODO: Setup actual user that logs in for the contrainers.
//        lifecycleScope.launch {
        val currentUserId = 1
        setupContainerTypeDropDownActv(currentUserId)


        // SEtting up the save button
        binding.saveBtn.setOnClickListener {
            validateAndAddIngredient()

        }

        // Setup add image button
        binding.cameraBtn.setOnClickListener {
            popupDialogForTakePhoto()
        }

        // TODO: Make image selected
        binding.editIconBtn.setOnClickListener { selectedImage ->
            IconPickerDialogFragment { selectedImage ->
                // Update image selected
                selectedIconId = selectedImage
                binding.iconImageIv.setImageResource(selectedImage)
            }.show(parentFragmentManager, "iconPickerDialog")

        }

        // TODO: Create editIcon click listener
        setupBarcodeLauncher()
        binding.scanQRBtn.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            barcodeLauncher.launch(cameraIntent)
        }
    }
    private fun validateAndAddIngredient() {
        var isValid = true

        fun markInvalid(view: View, hint: String) {
            view.requestFocus()
            if (view is Button) return
            if (view is androidx.appcompat.widget.AppCompatEditText) {
                view.setText("")
                view.hint = hint
                view.setHintTextColor(resources.getColor(android.R.color.holo_red_light, null))
            }
            isValid = false
        }

        val name = binding.itemNameEt.text.toString().trim()
        if (name.isEmpty()) markInvalid(binding.itemNameEt, "Name required")

        val quantityText = binding.itemNumberEt.text.toString().trim()
        val quantity = quantityText.toDoubleOrNull()
        if (quantity == null) markInvalid(binding.itemNumberEt, "Enter quantity")

        val unit = binding.unitTypeDropDownActv.text.toString().trim()
        if (unit.isEmpty()) markInvalid(binding.unitTypeDropDownActv, "Select unit")

        val type = binding.itemTypeDropDownActv.text.toString().trim()
        if (type.isEmpty()) markInvalid(binding.itemTypeDropDownActv, "Select type")

        val priceText = binding.priceEt.text.toString().replace("Php", "").trim()
        val price = priceText.toDoubleOrNull()
        if (price == null) markInvalid(binding.priceEt, "Enter price")

        val dateAdded = binding.dateBoughtEt.text.toString().trim()
        if (dateAdded.isEmpty()) markInvalid(binding.dateBoughtEt, "Set date bought")

        val expiration = binding.expirationDateEt.text.toString().trim()
        if (expiration.isEmpty()) markInvalid(binding.expirationDateEt, "Set expiration date")

        val condition = when {
            binding.radioVeryOk.isChecked -> "very ok"
            binding.radioStillOk.isChecked -> "still ok"
            binding.radioSlightlyNotOk.isChecked -> "slightly not ok"
            binding.radioNotOk.isChecked -> "not ok"
            else -> {
                Toast.makeText(requireContext(), "Please select a condition.", Toast.LENGTH_SHORT).show()
                isValid = false
                ""
            }
        }

        val containerType = binding.containerTypeDropDownActv.text.toString()
        if(idToNameMap[originalContainerId] != containerType){
            containerViewModel.decreaseCurrCap(selectedIngredient.attachedContainerId)
            containerViewModel.increaseCurrCap(selectedContainerId)

            containerViewModel.syncUpdatedContainerCapacity(originalContainerId, requireContext())
            containerViewModel.syncUpdatedContainerCapacity(selectedContainerId, requireContext())
        }else{
            selectedContainerId = originalContainerId
        }

        if (!isValid) {
            Toast.makeText(requireContext(), "Please complete all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Save into database
        val newIngredientEntity = IngredientEntity(
            ingredientID = selectedIngredient.ingredientID,
            iconResId = selectedIconId,
            name = name,
            quantity = quantity!!,
            unit = unit,
            itemType = type,
            price = price!!,
            conditionType = condition,
            dateAdded = dateAdded,
            expirationDate = expiration,
            imageList = selectedIngredient.imageList,
            attachedContainerId = selectedContainerId
        )

        groceryViewModel.updateGrocery(newIngredientEntity)
        Toast.makeText(requireContext(), "Grocery Edited!", Toast.LENGTH_SHORT).show()
        groceryViewModel.syncUpdatedIngredient(newIngredientEntity, requireContext())
        findNavController().navigateUp()

    }
    private fun setupBarcodeLauncher(){
        barcodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == AppCompatActivity.RESULT_OK) {
                val image = result.data?.extras?.get("data") as? Bitmap
                image?.let { bitmap ->
                    scanBarcodeFromBitmap(bitmap)
                }
            }
        }
    }

    // QR scanning
    private fun scanBarcodeFromBitmap(bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                if (barcodes.isNotEmpty()) {
                    val rawValue = barcodes[0].rawValue ?: "No data"
                    rawValue?.let { code ->
                        Log.d("Barcode", "Scanned: $code")
                        fetchBrandFromBarcode(code) // ← Call API here
                        Toast.makeText(requireContext(), "Scanned: $code", Toast.LENGTH_SHORT).show()

                    }

                } else {
                    showToast("No barcode detected.")
                }
            }
            .addOnFailureListener {
                showToast("Failed to scan: ${it.localizedMessage}")
            }
    }

    fun fetchBrandFromBarcode(barcode: String) {
        lifecycleScope.launch {
            try {
                val response = api.getProduct(barcode)
                if (response.isSuccessful) {
                    val product = response.body()?.product

                    binding.itemNameEt.setText(product?.productNameEN ?: "")
                    binding.itemNumberEt.setText(product?.quantity ?: "")
                    binding.unitTypeDropDownActv.setText(product?.unit ?: "")
                    binding.expirationDateEt.setText(product?.expirationDate ?: "")

                    Toast.makeText(requireContext(), "Product: ${product?.productNameEN ?: "Unknown"}", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "API Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Network Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private fun popupDialogForTakePhoto(){

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

    private fun setupTakePhotoFromGalleryOrCamera(){

        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {

                selectedIngredient.imageList.add(ImageRaw.fromUri(requireContext(), tempCameraImageUri!!)!!)
                binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(selectedIngredient.imageList.size - 1)
            }
        }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && tempCameraImageUri != null) {

                    selectedIngredient.imageList.add(ImageRaw.fromUri(requireContext(), tempCameraImageUri!!)!!)
                    binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(selectedIngredient.imageList.size - 1)
                }
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
    private fun setupIngredientView(ingredient: IngredientEntity) {
        // Setup the selected container
        if(ingredient.iconResId == -1){
            binding.iconImageIv.setImageResource(R.mipmap.ic_itemtype_other)

        }else{
            binding.iconImageIv.setImageResource(ingredient.iconResId)
            selectedIconId = ingredient.iconResId

        }
        binding.itemNameEt.setText(ingredient.name)
        binding.itemNumberEt.setText(ingredient.quantity.toString())
        binding.unitTypeDropDownActv.setText(ingredient.unit)
        binding.itemTypeDropDownActv.setText(ingredient.itemType)
        val formatted = getString(R.string.formatted_price, ingredient.price)
        binding.priceEt.setText(formatted)
        binding.dateBoughtEt.setText(ingredient.dateAdded)
        binding.expirationDateEt.setText(ingredient.expirationDate)
        binding.containerTypeDropDownActv.setText(idToNameMap[ingredient.attachedContainerId])

        when (ingredient.conditionType.lowercase()) {
            "very ok" -> binding.radioVeryOk.isChecked = true
            "still ok" -> binding.radioStillOk.isChecked = true
            "slightly not ok" -> binding.radioSlightlyNotOk.isChecked = true
            "not ok" -> binding.radioNotOk.isChecked = true
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

    private fun setupContainerTypeDropDownActv(userId: Int) {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                containerViewModel.readAllData.collect { containerList ->
                    if (containerList.isEmpty()) {
                        Toast.makeText(requireContext(), "No containers found.", Toast.LENGTH_SHORT)
                            .show()
                    }

                    // Build ID-to-name and name-to-ID maps
                    idToNameMap = containerList.associate { it.containerId to it.name }
                    val nameToIdMap = containerList.associate { it.name to it.containerId }

                    // Set the selected container ID to original
                    selectedContainerId = originalContainerId

                    val containerNames = containerList.map { it.name }
                    val adapter = ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item_for_update,
                        containerNames
                    )
                    binding.containerTypeDropDownActv.setAdapter(adapter)

                    // Pre-fill the dropdown field with the original container's name
                    val originalName = idToNameMap[originalContainerId]
                    binding.containerTypeDropDownActv.setText(originalName, false)

                    // Set up listener for dropdown selection
                    binding.containerTypeDropDownActv.setOnItemClickListener { _, _, position, _ ->
                        val selectedName = containerNames[position]
                        selectedContainerId = nameToIdMap[selectedName] ?: originalContainerId
                    }
                }
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

    }
    private fun setupRecycler() {

        args.currGrocery.let {
            binding.imagesRecyclerViewRv.adapter =
                GroceryViewImageGridAdapter(it.imageList, true)
        }
        binding.imagesRecyclerViewRv.layoutManager = GridLayoutManager(requireContext(), 2)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}