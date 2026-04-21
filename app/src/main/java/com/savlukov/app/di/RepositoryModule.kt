package com.savlukov.app.di

import com.savlukov.app.data.repository.FurnitureRepositoryImpl
import com.savlukov.app.domain.repository.FurnitureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFurnitureRepository(
        furnitureRepositoryImpl: FurnitureRepositoryImpl
    ): FurnitureRepository

    @Binds
    @Singleton
    abstract fun bindLoyaltyRepository(
        loyaltyRepositoryImpl: LoyaltyRepositoryImpl
    ): LoyaltyRepository

    @Binds
    @Singleton
    abstract fun bindStoriesRepository(
        storiesRepositoryImpl: StoriesRepositoryImpl
    ): StoriesRepository
}
