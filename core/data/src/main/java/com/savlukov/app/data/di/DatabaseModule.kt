package com.savlukov.app.data.di

import android.content.Context
import androidx.room.Room
import com.savlukov.app.data.local.FurnitureDao
import com.savlukov.app.data.local.FabricDao
import com.savlukov.app.data.local.StoryDao
import com.savlukov.app.data.local.SavlukovDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SavlukovDatabase {
        return Room.databaseBuilder(
            context,
            SavlukovDatabase::class.java,
            "savlukov_db"
        ).build()
    }

    @Provides
    fun provideFurnitureDao(database: SavlukovDatabase): FurnitureDao {
        return database.furnitureDao()
    }

    @Provides
    fun provideFabricDao(database: SavlukovDatabase): FabricDao {
        return database.fabricDao()
    }

    @Provides
    fun provideStoryDao(database: SavlukovDatabase): StoryDao {
        return database.storyDao()
    }
}
