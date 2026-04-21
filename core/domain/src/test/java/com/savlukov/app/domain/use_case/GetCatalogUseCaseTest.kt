package com.savlukov.app.domain.use_case

import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.util.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCatalogUseCaseTest {

    private val repository = mockk<FurnitureRepository>()
    private val getCatalogUseCase = GetCatalogUseCase(repository)

    @Test
    fun `invoke should return catalog from repository`() = runTest {
        // Arrange
        val furnitureList = listOf(
            Furniture("1", "Sofa", "Comfy", "Living Room", "url1", "ar1"),
            Furniture("2", "Chair", "Strong", "Office", "url2", "ar2")
        )
        every { repository.getCatalog() } returns flowOf(Resource.Success(furnitureList))

        // Act
        val result = getCatalogUseCase(Unit).toList()

        // Assert
        assertEquals(1, result.size)
        assert(result[0] is Resource.Success)
        assertEquals(furnitureList, (result[0] as Resource.Success).data)
    }

    @Test
    fun `invoke should return error when repository fails`() = runTest {
        // Arrange
        val errorMessage = "Network Error"
        every { repository.getCatalog() } returns flowOf(Resource.Error(errorMessage))

        // Act
        val result = getCatalogUseCase(Unit).toList()

        // Assert
        assertEquals(1, result.size)
        assert(result[0] is Resource.Error)
        assertEquals(errorMessage, (result[0] as Resource.Error).message)
    }
}
