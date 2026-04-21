package com.savlukov.app.data.di

import com.savlukov.app.data.repository.MockFurnitureRepository
import com.savlukov.app.data.repository.MockLoyaltyRepository
import com.savlukov.app.data.repository.MockStoriesRepository
import com.savlukov.app.data.repository.SignalRepositoryImpl
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.repository.LoyaltyRepository
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.domain.repository.SignalRepository
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
        mockFurnitureRepository: MockFurnitureRepository
    ): FurnitureRepository

    @Binds
    @Singleton
    abstract fun bindLoyaltyRepository(
        mockLoyaltyRepository: MockLoyaltyRepository
    ): LoyaltyRepository

    @Binds
    @Singleton
    abstract fun bindStoriesRepository(
        mockStoriesRepository: MockStoriesRepository
    ): StoriesRepository

    @Binds
    @Singleton
    abstract fun bindSignalRepository(
        signalRepositoryImpl: SignalRepositoryImpl
    ): SignalRepository
}
