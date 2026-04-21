package com.savlukov.app.data.repository

import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.model.StorySegment
import com.savlukov.app.domain.repository.StoriesRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockStoriesRepository @Inject constructor() : StoriesRepository {
    override fun getStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading())
        delay(400)
        emit(Resource.Success(listOf(
            Story(
                id = "s1", 
                title = "The Onyx Collection", 
                imageUrl = "https://api.savlukov.com/media/s1_thumb.webp", 
                date = "2026-04-18",
                segments = listOf(
                    StorySegment("seg1", "IMAGE", "https://api.savlukov.com/media/s1_1.webp", targetProductId = "sofa_onyx", ctaText = "Experience the Look"),
                    StorySegment("seg2", "IMAGE", "https://api.savlukov.com/media/s1_2.webp", targetProductId = "chair_aurum", ctaText = "View in Detail")
                )
            ),
            Story(
                id = "s2", 
                title = "Artisanal Craft", 
                imageUrl = "https://api.savlukov.com/media/s2_thumb.webp", 
                date = "2026-04-17",
                segments = listOf(
                    StorySegment("seg3", "VIDEO", "https://api.savlukov.com/media/craft_video.mp4", textOverlay = "Handcrafted in Belarus")
                )
            )
        )))
    }

    override suspend fun markStoryAsWatched(id: String) {
        delay(100)
    }
}
