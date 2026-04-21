package com.savlukov.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FurnitureEntity::class, FabricEntity::class, StoryEntity::class], version = 3)
abstract class SavlukovDatabase : RoomDatabase() {
    abstract fun furnitureDao(): FurnitureDao
    abstract fun fabricDao(): FabricDao
    abstract fun storyDao(): StoryDao
}
