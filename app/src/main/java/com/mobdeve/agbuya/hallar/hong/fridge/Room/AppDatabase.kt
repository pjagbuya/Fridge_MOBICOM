package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdeve.agbuya.hallar.hong.fridge.converters.BitmapTypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.converters.Converters
import com.mobdeve.agbuya.hallar.hong.fridge.converters.ImageContainerTypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.dao.*
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.*
import com.mobdeve.agbuya.hallar.hong.fridge.Room.*

@Database(
    entities = [
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        UserEntity::class,
        ContainerEntity::class,
        IngredientEntity::class,
        ContainerImageEntity::class,
        InventoryEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(
    BitmapTypeConverter::class,
    Converters::class,
    ImageContainerTypeConverter::class,
    Converters::class // Important for handling ArrayLists in InventoryEntity
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun ingredientDao(): IngredientDao
    abstract fun containerDao(): ContainerDao
    abstract fun recipeIngredientDao(): RecipeIngredientDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
