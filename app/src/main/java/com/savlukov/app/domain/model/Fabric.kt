package com.savlukov.app.domain.model

data class Fabric(
    val id: String,
    val name: String,
    val thumbnail: String,
    val textureUrl: String? = null,
    val colorHex: String? = null
)
