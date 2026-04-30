package com.savlukov.app.data.di

import android.content.Context
import com.savlukov.app.data.local.FavoriteDao
import com.savlukov.app.data.local.StoryDao
import com.savlukov.app.data.remote.InstagramApi
import com.savlukov.app.data.remote.StoriesApi
import com.savlukov.app.data.repository.MockFurnitureRepository
import com.savlukov.app.data.repository.MockLoyaltyRepository
import com.savlukov.app.data.repository.MockStoriesRepository
import com.savlukov.app.data.repository.SignalRepositoryImpl
import com.savlukov.app.data.repository.StoriesRepositoryImpl
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.repository.LoyaltyRepository
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.domain.repository.SignalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideFurnitureRepository(
        context: Context,
        favoriteDao: FavoriteDao
    ): FurnitureRepository = MockFurnitureRepository(context, favoriteDao)

    @Provides
    @Singleton
    fun provideLoyaltyRepository(): LoyaltyRepository = MockLoyaltyRepository()

    @Provides
    @Singleton
    fun provideStoriesRepository(): StoriesRepository = MockStoriesRepository()

    @Provides
    @Singleton
    fun provideStoriesRepository(
        api: StoriesApi,
        instagramApi: InstagramApi,
        dao: StoryDao
    ): StoriesRepository {
        return StoriesRepositoryImpl(api, instagramApi, dao)
    }
