package com.mobdeve.agbuya.hallar.hong.fridge.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.ImageContainer
import com.mobdeve.agbuya.hallar.hong.fridge.atomicClasses.Ingredient
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import androidx.navigation.fragment.NavHostFragment
import com.mobdeve.agbuya.hallar.hong.fridge.Room.UserEntity
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.ActivityMainBinding
import com.mobdeve.agbuya.hallar.hong.fridge.databinding.NavigationbarBinding
import com.mobdeve.agbuya.hallar.hong.fridge.domain.ContainerModel
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.viewModel.GrocerySharedViewModel
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
        val containerDao = db.containerDao()
        val ingredientDao = db.ingredientDao()

        CoroutineScope(Dispatchers.IO).launch {

            val existingUsers = userDao.getAllUsers()
            if (existingUsers.isNotEmpty()) {
                Log.d("SampleData", "Sample data already initialized. Skipping.")
                return@launch
            }
            // Insert one user
            val userIdLong = userDao.insertUser(
                UserEntity(
                    name = "Paul",
                    email = "p@gmail.com",
                    password = "123456"
                )
            )
            val userId = userIdLong.toInt() // Works safely now
            // Insert 5 containers
            val containerIds = (1..5).map {
                val container = ContainerEntity(
                    name = "Container $it",
                    imageContainer = ImageContainer(
                        resId = R.drawable.container_type_1_fridge,
                        colorId = R.color.black
                    ),
                    currCap = 0,
                    maxCap = 10,
                    timeStamp = ContainerModel.getTimeStamp(),
                    ownerUserId = userId
                )
                containerDao.insertContainer(container)
            }

            // Insert 5 ingredients (1 each container)
            containerIds.forEachIndexed { index, containerId ->
                val ingredient = IngredientEntity(
                    name = "Ingredient ${index + 1}",
                    iconResId = R.drawable.ic_launcher_foreground,
                    price = 10.0,
                    quantity = 10.0,
                    unit = "kg",
                    conditionType = "Fresh",
                    itemType = "VEGETABLE",
                    dateAdded = "2025-07-15",
                    expirationDate = "2025-08-15",
                    attachedContainerId = containerId.toInt(),
                    imageList = arrayListOf() // Or dummy ImageRaw
                )
                ingredientDao.insertIngredient(ingredient)
            }
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