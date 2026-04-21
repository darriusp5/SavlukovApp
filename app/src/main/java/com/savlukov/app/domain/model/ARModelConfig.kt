package com.savlukov.app.domain.model

data class ARModelConfig(
    val modelUrl: String,
    val activeMaterials: Map<String, FurnitureMaterial> = emptyMap()
)
