package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobdeve.agbuya.hallar.hong.fridge.converters.BitmapTypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.converters.Converters
import com.mobdeve.agbuya.hallar.hong.fridge.converters.ImageContainerTypeConverter
import com.mobdeve.agbuya.hallar.hong.fridge.dao.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.dao.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerImageEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity

@TypeConverters(BitmapTypeConverter::class, Converters::class, ImageContainerTypeConverter::class)
//this is the database, put all entities here
@Database(
    entities = [
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        UserEntity::class,
        InventoryEntity::class,
        MemberEntity::class, ContainerEntity::class, IngredientEntity::class, ContainerImageEntity::class
    ],
    version = 6,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao

    // Paul's side
    abstract fun ingredientDao(): IngredientDao
    abstract fun containerDao(): ContainerDao
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