package com.savlukov.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Story(
    val id: String,
    val title: String,
    val brandName: String = "Savlukov",
    val brandLogoUrl: String = "",
    val imageUrl: String, // Thumbnail
    val date: String,
    val isWatched: Boolean = false,
    val durationSeconds: Int = 5,
    val segments: List<StorySegment> = emptyList()
)

@Immutable
data class StorySegment(
    val id: String,
    val type: String, // IMAGE or VIDEO
    val contentUrl: String,
    val textOverlay: String? = null,
    val targetProductId: String? = null,
    val ctaText: String? = null
)
