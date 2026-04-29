package com.savlukov.app.data.repository

import android.content.Context
import com.savlukov.app.data.parser.CatalogParser
import com.savlukov.app.data.local.FavoriteDao
import com.savlukov.app.data.local.FavoriteEntity
import com.savlukov.app.domain.model.Category
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockFurnitureRepository(
    private val context: Context,
    private val favoriteDao: FavoriteDao
) : FurnitureRepository {
    private val mockFurniture = listOf(
        Furniture(
            "sofa_onyx",
            "Onyx Grand Sofa",
            "Deep-seated comfort meets architectural precision. Upholstered in premium Italian velvet.",
            "Sofas",
            "1",
            "https://api.savlukov.com/media/onyx_sofa.webp",
            "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/SheenChair/glTF-Binary/SheenChair.glb"
        ),
        Furniture(
            "chair_aurum",
            "Aurum Occasional Chair",
            "A sculptural masterpiece featuring hand-brushed brass accents and silk-blend upholstery.",
            "Chairs",
            "6",
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
        android.util.Log.e("SAVLUKOV_DEBUG", "=== REPOSITORY: getCatalog() CALLED ===")
        emit(Resource.Loading)
        delay(500)
        android.util.Log.e("SAVLUKOV_DEBUG", "Loading delay finished, starting parsing...")
        try {
            val parser = CatalogParser(context)
            val catalogData = parser.parseCatalog()
            android.util.Log.e("SAVLUKOV_DEBUG", "=== REPOSITORY: SUCCESS - Parsed ${catalogData.products.size} products and ${catalogData.categories.size} categories ===")
            if (catalogData.products.isEmpty()) {
                android.util.Log.e("SAVLUKOV_DEBUG", "CRITICAL: No products loaded! Categories: ${catalogData.categories.size}")
                android.util.Log.e("SAVLUKOV_DEBUG", "First few categories: ${catalogData.categories.take(3).joinToString { it.name }}")
                // TEMPORARY FALLBACK: Return mock data if parsing fails
                android.util.Log.e("SAVLUKOV_DEBUG", "Using fallback mock data!")
                emit(Resource.Success(mockFurniture))
            } else {
                emit(Resource.Success(catalogData.products))
            }
        } catch (e: Exception) {
            android.util.Log.e("SAVLUKOV_DEBUG", "CRITICAL: Failed to load catalog", e)
            // Fallback to mock data on error
            android.util.Log.e("SAVLUKOV_DEBUG", "Using fallback mock data due to error!")
            emit(Resource.Success(mockFurniture))
        }
    }

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading)
        delay(300)
        try {
            val parser = CatalogParser(context)
            val catalogData = parser.parseCatalog()
            if (catalogData.categories.isEmpty()) {
                android.util.Log.w("MockFurnitureRepository", "No categories loaded from XML, using fallback")
                val mockCategories = listOf(
                    Category("1", "Диваны", null),
                    Category("6", "Кресла", null)
                )
                emit(Resource.Success(mockCategories))
            } else {
                emit(Resource.Success(catalogData.categories))
            }
        } catch (e: Exception) {
            android.util.Log.e("MockFurnitureRepository", "Failed to load categories", e)
            // Fallback categories
            val fallbackCategories = listOf(
                Category("1", "Диваны", null),
                Category("6", "Кресла", null)
            )
            emit(Resource.Success(fallbackCategories))
        }
    }

    override fun getFurnitureDetail(id: String): Flow<Resource<Furniture>> = flow {
        emit(Resource.Loading)
        delay(300)
        try {
            val parser = CatalogParser(context)
            val catalogData = parser.parseCatalog()
            val item = catalogData.products.find { it.id == id }
            if (item != null) {
                emit(Resource.Success(item))
            } else {
                emit(Resource.Error("Furniture item not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load product: ${e.message}"))
        }
    }

    override fun getFabrics(): Flow<Resource<List<Fabric>>> = flow {
        emit(Resource.Loading)  // Убрали ()
        delay(200)
        emit(Resource.Success(mockFabrics))
    }

    override fun getFavorites(): Flow<Resource<List<Furniture>>> = flow {
        emit(Resource.Loading)
        try {
            favoriteDao.getAllFavorites().collect { favoriteEntities ->
                val catalogData = CatalogParser(context).parseCatalog()
                val favoriteIds = favoriteEntities.map { it.id }.toSet()
                val favorites = catalogData.products.filter { it.id in favoriteIds }
                emit(Resource.Success(favorites))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Failed to load favorites: ${e.message}"))
        }
    }

    override suspend fun addToFavorites(id: String) {
        favoriteDao.addFavorite(FavoriteEntity(id))
    }

    override suspend fun removeFromFavorites(id: String) {
        favoriteDao.removeFavorite(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return favoriteDao.isFavorite(id) > 0
    }

    override suspend fun refreshCatalog() {
        delay(1000)
    }
}
