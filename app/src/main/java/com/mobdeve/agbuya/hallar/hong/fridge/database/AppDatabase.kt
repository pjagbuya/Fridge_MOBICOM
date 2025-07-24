package com.mobdeve.agbuya.hallar.hong.fridge.database

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
import com.mobdeve.agbuya.hallar.hong.fridge.dao.UserDao
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.ContainerImageEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.IngredientEntity
import com.mobdeve.agbuya.hallar.hong.fridge.rooms.UserEntity


@TypeConverters(BitmapTypeConverter::class, Converters::class, ImageContainerTypeConverter::class)
@Database(
    entities = [UserEntity::class, ContainerEntity::class, IngredientEntity::class, ContainerImageEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ingredientDao(): IngredientDao
    abstract fun containerDao(): ContainerDao
    abstract fun userDao(): UserDao

    // Optional if you're using ContainerImageEntity directly
    // abstract fun containerImageDao(): ContainerImageDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fridge_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
