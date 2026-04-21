package com.savlukov.app.data.repository

import com.savlukov.app.data.local.FabricDao
import com.savlukov.app.data.local.FurnitureDao
import com.savlukov.app.data.mapper.toDomain
import com.savlukov.app.data.mapper.toEntity
import com.savlukov.app.data.remote.FurnitureApi
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class FurnitureRepositoryImpl @Inject constructor(
    private val api: FurnitureApi,
    private val furnitureDao: FurnitureDao,
    private val fabricDao: FabricDao
) : FurnitureRepository {

    override fun getCatalog(): Flow<Resource<List<Furniture>>> = channelFlow {
        send(Resource.Loading)

        // Launch refresh in background to not block initial cache emission
        launch {
            refreshCatalog()
        }

        // Observe local database as the single source of truth
        furnitureDao.getAllFurniture()
            .map { entities ->
                Resource.Success(entities.map { it.toDomain() })
            }
            .collect { 
                send(it) 
            }
    }.catch { e ->
        emit(Resource.Error(e.message ?: "Unknown Error", e))
    }.flowOn(Dispatchers.IO)

    override fun getFurnitureDetail(id: String): Flow<Resource<Furniture>> = flow {
        val cached = furnitureDao.getFurnitureById(id)
        if (cached != null) {
            emit(Resource.Success(cached.toDomain()))
        } else {
            emit(Resource.Error("Item not found"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getFabrics(): Flow<Resource<List<Fabric>>> = channelFlow {
        send(Resource.Loading)
        
        // Optionally launch refreshFabrics() here
        
        fabricDao.getAllFabrics()
            .map { entities ->
                Resource.Success(entities.map { it.toDomain() })
            }
            .collect { send(it) }
    }.flowOn(Dispatchers.IO)

    override suspend fun refreshCatalog() {
        try {
            val remoteData = api.getFurnitureList()
            furnitureDao.insertFurniture(remoteData.map { it.toEntity() })
        } catch (e: Exception) {
            // Error handling
        }
    }
}
