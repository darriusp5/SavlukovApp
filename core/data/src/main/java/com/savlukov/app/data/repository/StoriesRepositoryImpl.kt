package com.savlukov.app.data.repository

import com.savlukov.app.data.local.StoryDao
import com.savlukov.app.data.mapper.toDomain
import com.savlukov.app.data.mapper.toEntity
import com.savlukov.app.data.remote.StoriesApi
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class StoriesRepositoryImpl @Inject constructor(
    private val api: StoriesApi,
    private val instagramApi: InstagramApi,
    private val dao: StoryDao
) : StoriesRepository {

    // Instagram Access Token from Graph API Explorer
    private val instagramAccessToken = "EAAUrTvfg4bABRcZBqLFfA2YsJicb7uaWwAxg4ZAeVgt8rSt6zIgZC9YCZAJvBq27WuZC6CZBMsKaWlLmsSStE9uk6mv2P6ZBgcFUhg6nI9lT9InVvRTfwtAnZClLjRZABZCZAGnJcmkwOunX1HlhPt0N7gPc9CeFLRCdk2LZAfjXxZBSGKBxfy0FUFhs1lmDiplkQbw2TfQGaAomqVmcONmaYu5nCZAFBYWN3JIa2r3DB33wZDZD"

    override fun getStories(): Flow<Resource<List<Story>>> = channelFlow {
        send(Resource.Loading)

        launch {
            try {
                // Try to get Instagram Stories first
                val instagramStories = getInstagramStories()
                if (instagramStories.isNotEmpty()) {
                    send(Resource.Success(instagramStories))
                    return@launch
                }

                // Fallback to local API if Instagram fails
                try {
                    val remoteStories = api.getStories().stories.map { it.toDomain() }
                    dao.insertStories(remoteStories.map { it.toData() })
                    send(Resource.Success(remoteStories))
                } catch (e: Exception) {
                    // If both Instagram and local API fail, try cache
                    val cachedStories = dao.getAllStories().map { it.toDomain() }
                    if (cachedStories.isNotEmpty()) {
                        send(Resource.Success(cachedStories))
                    } else {
                        send(Resource.Error("Не удалось загрузить истории из Instagram и локального API"))
                    }
                }

            } catch (e: Exception) {
                // Fallback to cached stories
                try {
                    val cachedStories = dao.getAllStories().map { it.toDomain() }
                    if (cachedStories.isNotEmpty()) {
                        send(Resource.Success(cachedStories))
                    } else {
                        send(Resource.Error("Ошибка загрузки историй: ${e.localizedMessage}"))
                    }
                } catch (cacheException: Exception) {
                    send(Resource.Error("Все источники историй недоступны"))
                }
            }
        }
    }

    private suspend fun getInstagramStories(): List<Story> {
        try {
            // First check if we have access to the Instagram account
            val userResponse = instagramApi.getUserInfo(accessToken = instagramAccessToken)
            if (!userResponse.isSuccessful) {
                throw Exception("Instagram API access error: ${userResponse.message()}")
            }

            // Now try to get stories first, if no stories, get recent media
            var response = instagramApi.getStories(accessToken = instagramAccessToken)

            // If no stories, fallback to recent media
            if (!response.isSuccessful || response.body()?.data.isNullOrEmpty()) {
                response = instagramApi.getMedia(accessToken = instagramAccessToken)
            }

            if (response.isSuccessful) {
                val instagramData = response.body()?.data ?: emptyList()

                return instagramData.mapNotNull { instagramStory ->
                    // Convert Instagram story to our Story model
                    if (instagramStory.mediaUrl != null || instagramStory.thumbnailUrl != null) {
                        Story(
                            id = instagramStory.id,
                            title = instagramStory.caption ?: "Instagram Story",
                            imageUrl = instagramStory.mediaUrl ?: instagramStory.thumbnailUrl ?: "",
                            videoUrl = if (instagramStory.mediaType == "VIDEO") instagramStory.mediaUrl else null,
                            isVideo = instagramStory.mediaType == "VIDEO",
                            duration = 5000L, // Default 5 seconds for stories
                            createdAt = instagramStory.timestamp ?: "",
                            isActive = true,
                            permalink = instagramStory.permalink,
                            source = "instagram"
                        )
                    } else null
                }
            } else {
                throw Exception("Instagram API error: ${response.message()} (Code: ${response.code()})")
            }
        } catch (e: Exception) {
            throw e
        }
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
