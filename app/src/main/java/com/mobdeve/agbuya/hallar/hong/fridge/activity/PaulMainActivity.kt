package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.container.GroceryDataHelper
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.RecipeIngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.NavigationbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.InventoryEntity
import com.mobdeve.agbuya.hallar.hong.fridge.sharedModels.GrocerySharedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaulMainActivity : AppCompatActivity() {
    companion object{
        const val EDIT_TYPE_KEY = "EDIT_TYPE"

    }
    private lateinit var  activityMainBinding : ActivityMainBinding

    private lateinit var  containerModels: ArrayList<ContainerModel>
    private lateinit var navBarBinding: NavigationbarBinding

    private lateinit var  groceryModels: ArrayList<Ingredient>
    private val groceryViewModel: GrocerySharedViewModel by viewModels()
//    private val newContainerResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        // Check to see if the result returned is appropriate (i.e. OK)
//        if (result.resultCode == RESULT_OK) {
//            // TODO get back the changes to the recycler view and correspondingly add it
//
//
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

            activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(activityMainBinding.root)
            Log.d("ViewModelTest", "GroceryViewModel instance: $groceryViewModel")

            initSampleData(applicationContext)
            groceryViewModel.loadGroceryList(applicationContext)
            groceryViewModel.groceryList.observe(this) { list ->
                groceryModels = ArrayList(list)
            }
            navBarBinding = activityMainBinding.navigationBar


            setupNavigation()
            initSampleData(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dynamicContent) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    private fun wipeAllData(context: Context) {
        val db = AppDatabase.getInstance(context)
        CoroutineScope(Dispatchers.IO).launch {
            db.ingredientDao().deleteAll()
            db.containerDao().deleteAll()
//            db.userDao().deleteAll()
            Log.d("SampleData", "All data wiped.")
        }
    }
    private fun initSampleData(context: Context) {
        val db = AppDatabase.getInstance(context)
        val userDao = db.userDao()
        val inventoryDao = db.inventoryDao()
        val containerDao = db.containerDao()
        val ingredientDao = db.ingredientDao()
        val recipeDao = db.recipeDao()
        val recipeIngredientDao = db.recipeIngredientDao()

        CoroutineScope(Dispatchers.IO).launch {
//            // Skip if data already exists
//            if (userDao) {
//                Log.d("SampleData", "Sample data already initialized.")
//                return@launch
//            }

            // 1. Insert User
            val user = UserEntity(
                firebaseUid = "0",
                username = "Paul",
                ownedInventoryId = null,
                joinedInventoryId = null
            )
            userDao.insertUser(user)

            // 2. Insert Inventory
            val inventoryId = "inv_sample_001"
            val inventory = InventoryEntity(
                inventoryId = inventoryId,
                inventoryName = "Paul's Inventory",
                ownerId = user.firebaseUid
            )
            inventoryDao.insertInventory(inventory)

            // 3. Update User with ownedInventoryId
            userDao.insertUser(user.copy(ownedInventoryId = inventoryId))

            // 4. Insert Containers
            val containerIds = (1..3).map { i ->
                val container = ContainerEntity(
                    inventoryOwnerId = inventoryId,
                    name = "Container $i",
                    imageContainer = ImageContainer(
                        resId = R.drawable.container_type_1_fridge,
                        colorId = R.color.black
                    ),
                    currCap = 0,
                    maxCap = 10,
                    timeStamp = System.currentTimeMillis().toString()
                )
                containerDao.insertContainer(container)
            }

            // 5. Insert Ingredients (1 per container)
            val ingredientNames = mutableListOf<String>()
            val ingredientEntities = containerIds.mapIndexed { index, containerId ->
                val name = "Ingredient ${index + 1}"
                ingredientNames.add(name)
                IngredientEntity(
                    name = name,
                    iconResId = R.drawable.ic_launcher_foreground,
                    quantity = 2.0 * (index + 1),
                    price = 5.0 + index,
                    unit = "g",
                    conditionType = "Fresh",
                    itemType = "VEGETABLE",
                    dateAdded = "2025-07-01",
                    expirationDate = "2025-08-01",
                    attachedContainerId = containerId.toInt(),
                    imageList = arrayListOf()
                )
            }

            ingredientEntities.forEach { ingredientDao.insertIngredient(it) }

            // 6. Insert Recipe
            val recipe = RecipeEntity(
                inventoryOwnerId = inventoryId,
                name = "Easy Salad",
                description = "Simple veggie salad with fresh ingredients"
            )
            val recipeId = recipeDao.insertRecipe(recipe).toInt()

            // 7. Insert RecipeIngredientEntities referencing ingredients
            ingredientNames.forEachIndexed { index, name ->
                val recipeIngredient = RecipeIngredientEntity(
                    recipeId = recipeId,
                    name = name,
                    amount = 100.0 + index * 50,
                    unit = "g",
                    isCustom = false
                )
                recipeIngredientDao.insert(recipeIngredient)
            }

            Log.d("SampleData", "Sample user, inventory, containers, ingredients, and recipes inserted.")
        }
    }


    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.dynamicContent) as NavHostFragment
        val navController = navHostFragment.navController


        navBarBinding.containersBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.containersBtn.setImageResource(R.mipmap.container_dark)
            navController.navigate(R.id.containerMain)
        }
        navBarBinding.groceriesBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredients_dark)
            navController.navigate(R.id.groceriesMain)
        }
        navBarBinding.recipesBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_dark)
            navController.navigate(R.id.recipeMainFragment)
        }
        navBarBinding.profileBtn.setOnClickListener {
            resetAllIcons()
            navBarBinding.profileBtn.setImageResource(R.mipmap.profile_dark)
            navController.navigate(R.id.profileMain)
        }
    }

    fun resetAllIcons() {
        navBarBinding.containersBtn.setImageResource(R.mipmap.container_white)
        navBarBinding.groceriesBtn.setImageResource(R.mipmap.ingredient_white)
        navBarBinding.recipesBtn.setImageResource(R.mipmap.recipe_white)
        navBarBinding.profileBtn.setImageResource(R.mipmap.profile_white)
    }


}