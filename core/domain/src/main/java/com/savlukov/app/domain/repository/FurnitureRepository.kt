package com.savlukov.app.domain.repository

import com.savlukov.app.domain.model.Category
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FurnitureRepository {
    fun getCatalog(): Flow<Resource<List<Furniture>>>
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getFurnitureDetail(id: String): Flow<Resource<Furniture>>
    fun getFabrics(): Flow<Resource<List<Fabric>>>
    fun getFavorites(): Flow<Resource<List<Furniture>>>
    suspend fun addToFavorites(id: String)
    suspend fun removeFromFavorites(id: String)
    suspend fun isFavorite(id: String): Boolean
    suspend fun refreshCatalog()
}
