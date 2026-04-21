package com.savlukov.app.domain.model

data class FurnitureMaterial(
    val id: String,
    val name: String,
    val colorHex: String? = null,
    val textureUrl: String? = null
)
