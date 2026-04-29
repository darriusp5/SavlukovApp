package com.savlukov.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FurnitureEntity::class, FabricEntity::class, StoryEntity::class, FavoriteEntity::class],
    version = 4,
    exportSchema = false
)
abstract class SavlukovDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao
    abstract fun fabricDao(): FabricDao
    abstract fun storyDao(): StoryDao
    abstract fun favoriteDao(): FavoriteDao
}
