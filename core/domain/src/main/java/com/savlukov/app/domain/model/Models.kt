package com.savlukov.app.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Furniture(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val arModelUrl: String
)

@Immutable
data class Fabric(
    val id: String,
    val name: String,
    val thumbnail: String,
    val textureUrl: String = "",
    val colorHex: String? = null
)

@Immutable
data class Story(
    val id: String,
    val title: String,
    val brandName: String = "Savlukov",
    val brandLogoUrl: String = "",
    val imageUrl: String,
    val date: String,
    val isWatched: Boolean = false,
    val durationSeconds: Int = 5,
    val segments: List<StorySegment> = emptyList()
)

@Immutable
data class StorySegment(
    val id: String,
    val contentUrl: String,
    val targetProductId: String? = null
)
