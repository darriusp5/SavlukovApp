package com.savlukov.app.data.di

import com.savlukov.app.data.remote.FurnitureApi
import com.savlukov.app.data.remote.InstagramApi
import com.savlukov.app.data.remote.LoyaltyApi
import com.savlukov.app.data.remote.StoriesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.savlukov.by/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideFurnitureApi(retrofit: Retrofit): FurnitureApi {
        return retrofit.create(FurnitureApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLoyaltyApi(retrofit: Retrofit): LoyaltyApi {
        return retrofit.create(LoyaltyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideStoriesApi(retrofit: Retrofit): StoriesApi {
        return retrofit.create(StoriesApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInstagramRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://graph.instagram.com/v18.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideInstagramApi(instagramRetrofit: Retrofit): InstagramApi {
        return instagramRetrofit.create(InstagramApi::class.java)
    }
}
