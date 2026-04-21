package com.savlukov.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "furniture")
data class FurnitureEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val category: String,
    val imageUrl: String,
    val arModelUrl: String
)
