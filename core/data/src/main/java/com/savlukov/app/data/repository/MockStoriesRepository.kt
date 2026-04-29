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
    // Implemented Instagram stories parsing placeholder
    // To parse real stories from https://www.instagram.com/savlukovmebel?igsh=bGhtbDN5ejNqa3gw,
    // use webfetch to get HTML and parse JSON embedded in script tags for story data.
    override fun getStories(): Flow<Resource<List<Story>>> = flow {
        emit(Resource.Loading)
        delay(400)
        emit(Resource.Success(listOf(
            Story(
                id = "s1",
                title = "The Onyx Collection",
                brandName = "Savlukov",
                brandLogoUrl = "https://api.savlukov.com/media/logo.webp",
                imageUrl = "https://api.savlukov.com/media/s1_thumb.webp",
                date = "2026-04-18",
                segments = listOf(
                    StorySegment(
                        id = "seg1",
                        contentUrl = "https://api.savlukov.com/media/s1_1.webp",
                        targetProductId = "sofa_onyx",
                        textOverlay = "Experience the Look"  // ← textOverlay обязательный
                    ),
                    StorySegment(
                        id = "seg2",
                        contentUrl = "https://api.savlukov.com/media/s1_2.webp",
                        targetProductId = "chair_aurum",
                        textOverlay = "View in Detail"
                    )
                )
            ),
            Story(
                id = "s2",
                title = "Artisanal Craft",
                brandName = "Savlukov",
                brandLogoUrl = "https://api.savlukov.com/media/logo.webp",
                imageUrl = "https://api.savlukov.com/media/s2_thumb.webp",
                date = "2026-04-17",
                segments = listOf(
                    StorySegment(
                        id = "seg3",
                        contentUrl = "https://api.savlukov.com/media/craft_video.mp4",
                        textOverlay = "Handcrafted in Belarus"  // ← targetProductId не обязателен
                    )
                )
            )
        )))
    }

    override suspend fun markStoryAsWatched(id: String) {
        delay(100)
    }
}
