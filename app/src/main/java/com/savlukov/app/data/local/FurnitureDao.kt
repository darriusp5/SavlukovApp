package com.savlukov.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FurnitureDao {
    @Query("SELECT * FROM furniture")
    fun getAllFurniture(): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE category = :category")
    fun getFurnitureByCategory(category: String): Flow<List<FurnitureEntity>>

    @Query("SELECT * FROM furniture WHERE id = :id")
    suspend fun getFurnitureById(id: String): FurnitureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFurniture(furniture: List<FurnitureEntity>)

    @Query("DELETE FROM furniture")
    suspend fun clearAll()
}
