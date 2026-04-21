package com.savlukov.app.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FabricDao {
    @Query("SELECT * FROM fabrics")
    fun getAllFabrics(): Flow<List<FabricEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFabrics(fabrics: List<FabricEntity>)
}
