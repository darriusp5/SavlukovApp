package com.savlukov.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fabrics")
data class FabricEntity(
    @PrimaryKey val id: String,
    val name: String,
    val thumbnail: String,
    val textureUrl: String?,
    val colorHex: String?
)
