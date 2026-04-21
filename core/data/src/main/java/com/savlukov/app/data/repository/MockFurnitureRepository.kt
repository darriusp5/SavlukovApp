package com.savlukov.app.data.repository

import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MockFurnitureRepository @Inject constructor() : FurnitureRepository {
    private val mockFurniture = listOf(
        Furniture(
            "sofa_onyx", 
            "Onyx Grand Sofa", 
            "Deep-seated comfort meets architectural precision. Upholstered in premium Italian velvet.", 
            "Sofas", 
            "https://api.savlukov.com/media/onyx_sofa.webp", 
            "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF-Binary/SheenChair.glb"
        ),
        Furniture(
            "chair_aurum", 
            "Aurum Occasional Chair", 
            "A sculptural masterpiece featuring hand-brushed brass accents and silk-blend upholstery.", 
            "Chairs", 
            "https://api.savlukov.com/media/aurum_chair.webp", 
            "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF-Binary/SheenChair.glb"
        )
    )

    private val mockFabrics = listOf(
        Fabric("f1", "Royal Velvet - Midnight Blue", "https://api.savlukov.com/media/velvet_blue.webp", "#191970"),
        Fabric("f2", "Tuscan Linen - Sand", "https://api.savlukov.com/media/linen_sand.webp", "#F4A460"),
        Fabric("f3", "Milanese Leather - Cognac", "https://api.savlukov.com/media/leather_cognac.webp", "#964B00")
    )

    override fun getCatalog(): Flow<Resource<List<Furniture>>> = flow {
        emit(Resource.Loading())
        delay(500)
        emit(Resource.Success(mockFurniture))
    }

    override fun getFurnitureDetail(id: String): Flow<Resource<Furniture>> = flow {
        emit(Resource.Loading())
        delay(300)
        val item = mockFurniture.find { it.id == id }
        if (item != null) emit(Resource.Success(item)) else emit(Resource.Error("Furniture item not found"))
    }

    override fun getFabrics(): Flow<Resource<List<Fabric>>> = flow {
        emit(Resource.Loading())
        delay(200)
        emit(Resource.Success(mockFabrics))
    }

    override suspend fun refreshCatalog() {
        delay(1000)
    }
}
