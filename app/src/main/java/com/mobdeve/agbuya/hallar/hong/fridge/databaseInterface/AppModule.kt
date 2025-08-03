package com.mobdeve.agbuya.hallar.hong.fridge.databaseInterface
import android.content.Context
import androidx.room.Room
import com.mobdeve.agbuya.hallar.hong.fridge.Room.AppDatabase
import com.mobdeve.agbuya.hallar.hong.fridge.Room.ContainerDao
import com.mobdeve.agbuya.hallar.hong.fridge.Room.IngredientDao
import com.mobdeve.agbuya.hallar.hong.fridge.repository.ContainerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideIngredientDao(appDatabase: AppDatabase): IngredientDao {
        return appDatabase.ingredientDao()
    }

    @Provides
    fun provideContainerDao(appDatabase: AppDatabase): ContainerDao {
        return appDatabase.containerDao()
    }

    @Provides
    fun provideContainerRepository(containerDao: ContainerDao): ContainerRepository {
        return ContainerRepository(containerDao)
    }
}
