package com.savlukov.app.domain.repository

import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface FurnitureRepository {
    fun getCatalog(): Flow<Resource<List<Furniture>>>
    fun getFurnitureDetail(id: String): Flow<Resource<Furniture>>
    fun getFabrics(): Flow<Resource<List<Fabric>>>
    suspend fun refreshCatalog()
}
