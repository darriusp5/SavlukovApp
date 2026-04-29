package com.savlukov.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey val id: String,
    val title: String,
    val imageUrl: String,
    val date: String,
    val isWatched: Boolean,
    val durationSeconds: Int,
    val targetProductId: String?
)
