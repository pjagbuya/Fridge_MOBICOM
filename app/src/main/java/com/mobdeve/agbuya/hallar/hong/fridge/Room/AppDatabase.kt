package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//this is the database, put all entities here
@Database(
    entities = [
        RecipeEntity::class,
        RecipeIngredientEntity::class,
        UserEntity::class,
        InventoryEntity::class,
        MemberEntity::class
    ],
    version = 4,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun recipeDao(): RecipeDao
    abstract fun userDao(): UserDao
    abstract fun inventoryDao(): InventoryDao

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