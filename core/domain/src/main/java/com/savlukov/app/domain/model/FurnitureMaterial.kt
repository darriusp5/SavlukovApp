package com.savlukov.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FurnitureMaterial(
    val id: String,
    val name: String,
    val colorHex: String? = null,
    val textureUrl: String? = null
)
