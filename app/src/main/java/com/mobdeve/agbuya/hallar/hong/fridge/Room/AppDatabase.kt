package com.mobdeve.agbuya.hallar.hong.fridge.Room

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.converters.*

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

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE UserEntity ADD COLUMN password TEXT NOT NULL DEFAULT ''")
            }
        }

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

        // Migration Code template
//        fun getInstance(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "fridge_database"
//                )
//                    .addMigrations(MIGRATION_1_2)
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
    }
}