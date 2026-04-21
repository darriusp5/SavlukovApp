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
