package com.savlukov.app.data.mapper

import com.savlukov.app.data.local.FabricEntity
import com.savlukov.app.data.local.FurnitureEntity
import com.savlukov.app.data.local.StoryEntity
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.model.Story
import com.savlukov.app.domain.model.StorySegment

fun FurnitureEntity.toDomain() = Furniture(
    id = id,
    name = name,
    description = description,
    category = category,
    imageUrl = imageUrl,
    arModelUrl = arModelUrl
)

fun Furniture.toEntity() = FurnitureEntity(
    id = id,
    name = name,
    description = description,
    category = category,
    imageUrl = imageUrl,
    arModelUrl = arModelUrl
)

fun FabricEntity.toDomain() = Fabric(
    id = id,
    name = name,
    thumbnail = thumbnail,
    textureUrl = textureUrl,
    colorHex = colorHex
)

fun Fabric.toEntity() = FabricEntity(
    id = id,
    name = name,
    thumbnail = thumbnail,
    textureUrl = textureUrl,
    colorHex = colorHex
)

fun StoryEntity.toDomain() = Story(
    id = id,
    title = title,
    imageUrl = imageUrl,
    date = date,
    isWatched = isWatched,
    durationSeconds = durationSeconds,
    segments = listOf(
        StorySegment(
            id = "initial_$id",
            contentUrl = imageUrl,
            targetProductId = targetProductId
        )
    )
)

fun Story.toEntity() = StoryEntity(
    id = id,
    title = title,
    imageUrl = imageUrl,
    date = date,
    isWatched = isWatched,
    durationSeconds = durationSeconds,
    targetProductId = segments.firstOrNull()?.targetProductId
)
