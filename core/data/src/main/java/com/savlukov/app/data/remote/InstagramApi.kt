package com.savlukov.app.data.remote

import com.savlukov.app.data.remote.model.InstagramStoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface InstagramApi {

    @GET("me/stories")
    suspend fun getStories(
        @Query("fields") fields: String = "id,media_type,media_url,thumbnail_url,permalink,caption,timestamp",
        @Query("access_token") accessToken: String
    ): Response<InstagramStoryResponse>

    @GET("me/media")
    suspend fun getMedia(
        @Query("fields") fields: String = "id,media_type,media_url,thumbnail_url,permalink,caption,timestamp",
        @Query("access_token") accessToken: String
    ): Response<InstagramStoryResponse>

    @GET("me")
    suspend fun getUserInfo(
        @Query("fields") fields: String = "id,username,account_type",
        @Query("access_token") accessToken: String
    ): Response<Map<String, Any>>
}