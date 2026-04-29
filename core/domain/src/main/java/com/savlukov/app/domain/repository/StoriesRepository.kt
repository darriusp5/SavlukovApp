package com.savlukov.app.domain.repository

import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface StoriesRepository {
    fun getStories(): Flow<Resource<List<Story>>>
    suspend fun markStoryAsWatched(id: String)
}
