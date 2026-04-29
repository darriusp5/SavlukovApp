package com.savlukov.app.data.remote

import com.savlukov.app.domain.model.Story
import retrofit2.http.GET

interface StoriesApi {
    @GET("stories/curated")
    suspend fun getCuratedStories(): List<Story>
}
