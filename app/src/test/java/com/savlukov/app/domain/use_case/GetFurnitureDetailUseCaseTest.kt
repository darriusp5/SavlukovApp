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

class GetFurnitureDetailUseCaseTest {

    private val repository = mockk<FurnitureRepository>()
    private val getFurnitureDetailUseCase = GetFurnitureDetailUseCase(repository)

    @Test
    fun `invoke_validId_returnsSuccessWithFurniture`() = runTest {
        // Arrange
        val furnitureId = "1"
        val furniture = Furniture(furnitureId, "Sofa", "Comfy", "Living Room", 1000.0, "url1", "ar1")
        every { repository.getFurnitureDetail(furnitureId) } returns flowOf(Resource.Success(furniture))

        // Act
        val result = getFurnitureDetailUseCase(furnitureId).toList()

        // Assert
        assertEquals(1, result.size)
        assert(result[0] is Resource.Success)
        assertEquals(furniture, (result[0] as Resource.Success).data)
    }

    @Test
    fun `invoke_invalidId_returnsErrorResource`() = runTest {
        // Arrange
        val furnitureId = "999"
        val errorMessage = "Not found"
        every { repository.getFurnitureDetail(furnitureId) } returns flowOf(Resource.Error(errorMessage))

        // Act
        val result = getFurnitureDetailUseCase(furnitureId).toList()

        // Assert
        assertEquals(1, result.size)
        assert(result[0] is Resource.Error)
        assertEquals(errorMessage, (result[0] as Resource.Error).message)
    }
}
