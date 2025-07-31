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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.GroceryViewImageGridAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageRaw
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.OpenFoodFactsApi
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentAddBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.GroceryComponentUpdateBinding
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.ContainerSharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.UserViewModel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GroceryActivityFragmentAdd : Fragment() {

    private var _binding: GroceryComponentAddBinding? = null
    private val binding get() = _binding!!
    private var selectedIconId : Int = -1
    private var selectedContainerId : Int = -1


    private lateinit var imagesList: ArrayList<ImageRaw>

    // Camera setup
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Uri>

    // QR setup
    private lateinit var barcodeLauncher: ActivityResultLauncher<Intent>

    private lateinit var groceryViewModel : GrocerySharedViewModel
    private lateinit var containerViewModel : ContainerSharedViewModel


    private lateinit var idToNameMap : Map<Int, String>

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
    private fun readyForNew(){
        // create default/empty ingredient

        // TODO: Track whose containerID they will pick
        val newDate = Ingredient.getSimpleDateStamp()
        binding.headerActionItemLabelTv.text = "Add Grocery"
        binding.dateBoughtEt.setText(newDate)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GroceryComponentAddBinding.inflate(inflater, container, false)
        groceryViewModel = ViewModelProvider(this)[GrocerySharedViewModel::class.java]
        containerViewModel = ViewModelProvider(this)[ContainerSharedViewModel::class.java]
        imagesList = ArrayList<ImageRaw>()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Setup camera launcher states

        setupDropdowns()
        setupConditionRadios()
        setupTakePhotoFromGalleryOrCamera()

        readyForNew()


        setupRecycler()
        //Saving information for original container it was stored in

        // TODO: Setup actual user that logs in for the contrainers.
//        lifecycleScope.launch {
        val currentUserId = 1
        setupContainerTypeDropDownActv(currentUserId)



        binding.iconImageIv.setImageResource(R.mipmap.ic_itemtype_other)
        // Setup add image button
        binding.cameraBtn.setOnClickListener {
            popupDialogForTakePhoto()
        }

        binding.saveBtn.setOnClickListener {
            validateAndAddIngredient()
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
        val containerType = binding.containerTypeDropDownActv.text.toString().trim()
        if (containerType.isEmpty()) markInvalid(binding.containerTypeDropDownActv, "Set container")


        if (!isValid) {
            Toast.makeText(requireContext(), "Please complete all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        // Save into database
        val newIngredientEntity = IngredientEntity(
            iconResId = selectedIconId,
            name = name,
            quantity = quantity!!,
            unit = unit,
            itemType = type,
            price = price!!,
            conditionType = condition,
            dateAdded = dateAdded,
            expirationDate = expiration,
            imageList = imagesList,
            attachedContainerId = selectedContainerId
        )

        groceryViewModel.addGrocery(newIngredientEntity)
        Toast.makeText(requireContext(), "Grocery added!", Toast.LENGTH_SHORT).show()
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
    private val api: OpenFoodFactsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenFoodFactsApi::class.java)
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

                imagesList.add(ImageRaw.fromUri(requireContext(), uri)!!)
                binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(imagesList.size - 1)
            }
        }

        takePhotoLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && tempCameraImageUri != null) {

                    imagesList.add(ImageRaw.fromUri(requireContext(), tempCameraImageUri!!)!!)

                    binding.imagesRecyclerViewRv.adapter?.notifyItemInserted(imagesList.size - 1)
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
        containerViewModel.readAllData.observe(viewLifecycleOwner) { containerList ->
            if (containerList.isEmpty()) {
                Toast.makeText(requireContext(), "No containers found.", Toast.LENGTH_SHORT).show()
            }
            // Build ID-to-name and name-to-ID maps
            idToNameMap = containerList.associate { it.containerId to it.name }
            val nameToIdMap = containerList.associate { it.name to it.containerId }


            val containerNames = containerList.map { it.name }
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.dropdown_item_for_update,
                containerNames
            )
            binding.containerTypeDropDownActv.setAdapter(adapter)



            // Set up listener for future dropdown selection
            binding.containerTypeDropDownActv.setOnItemClickListener { _, _, position, _ ->
                val selectedName = containerNames[position]
                selectedContainerId = nameToIdMap[selectedName]!!
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


        binding.imagesRecyclerViewRv.adapter =
            GroceryViewImageGridAdapter(imagesList, true)

        binding.imagesRecyclerViewRv.layoutManager = GridLayoutManager(requireContext(), 2)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}