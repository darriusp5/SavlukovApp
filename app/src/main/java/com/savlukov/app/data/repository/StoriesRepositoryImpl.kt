package com.savlukov.app.data.repository

import com.savlukov.app.data.local.StoryDao
import com.savlukov.app.data.mapper.toDomain
import com.savlukov.app.data.mapper.toEntity
import com.savlukov.app.data.remote.StoriesApi
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val api: StoriesApi,
    private val dao: StoryDao
) : StoriesRepository {

    override fun getStories(): Flow<Resource<List<Story>>> = channelFlow {
        send(Resource.Loading)

        launch {
            try {
                val remoteData = api.getCuratedStories()
                dao.insertStories(remoteData.map { it.toEntity() })
            } catch (e: Exception) {
                // Background refresh fail is silent to use cache
            }
        }

        dao.getAllStories()
            .map { entities ->
                Resource.Success(entities.map { it.toDomain() })
            }
            .collect { send(it) }
    }.flowOn(Dispatchers.IO)

    override suspend fun markStoryAsWatched(id: String) {
        dao.markAsWatched(id)
    }
}
