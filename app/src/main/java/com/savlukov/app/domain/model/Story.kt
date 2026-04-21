package com.savlukov.app.domain.model

data class Story(
    val id: String,
    val title: String,
    val imageUrl: String,
    val date: String,
    val isWatched: Boolean = false,
    val durationSeconds: Int = 5,
    val targetProductId: String? = null
)
