package com.savlukov.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class InstagramStoryResponse(
    val data: List<InstagramStory>,
    val paging: InstagramPaging? = null
)

data class InstagramStory(
    val id: String,
    @SerializedName("media_type")
    val mediaType: String, // STORY, REEL, etc.
    @SerializedName("media_url")
    val mediaUrl: String? = null,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String? = null,
    val permalink: String? = null,
    val caption: String? = null,
    val timestamp: String? = null
)

data class InstagramPaging(
    val cursors: InstagramCursors? = null,
    val next: String? = null,
    val previous: String? = null
)

data class InstagramCursors(
    val before: String? = null,
    val after: String? = null
)