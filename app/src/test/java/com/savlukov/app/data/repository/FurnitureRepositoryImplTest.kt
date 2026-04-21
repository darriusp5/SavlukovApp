package com.savlukov.app.data.repository

import app.cash.turbine.test
import com.savlukov.app.data.local.FurnitureDao
import com.savlukov.app.data.local.FurnitureEntity
import com.savlukov.app.data.remote.FurnitureApi
import com.savlukov.app.domain.util.Resource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FurnitureRepositoryImplTest {

    private lateinit var api: FurnitureApi
    private lateinit var dao: FurnitureDao
    private lateinit var repository: FurnitureRepositoryImpl

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk()
        repository = FurnitureRepositoryImpl(api, dao)
    }

    @Test
    fun `getCatalog emits Loading and then Success from database`() = runTest {
        // Arrange
        val entities = listOf(
            FurnitureEntity("1", "Sofa", "Desc", "Cat", 100.0, "img", "ar")
        )
        every { dao.getAllFurniture() } returns flowOf(entities)
        coEvery { api.getFurnitureList() } returns emptyList() // Background refresh
        coEvery { dao.insertFurniture(any()) } returns Unit

        // Act & Assert
        repository.getCatalog().test {
            assertEquals(Resource.Loading, awaitItem())
            val success = awaitItem() as Resource.Success
            assertEquals(1, success.data.size)
            assertEquals("Sofa", success.data[0].name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refreshCatalog does not call clearAll`() = runTest {
        // Arrange
        coEvery { api.getFurnitureList() } returns emptyList()
        coEvery { dao.insertFurniture(any()) } returns Unit
        
        // Act
        repository.refreshCatalog()

        // Assert
        coVerify(exactly = 1) { api.getFurnitureList() }
        coVerify(exactly = 1) { dao.insertFurniture(any()) }
        coVerify(exactly = 0) { dao.clearAll() } // Critical regression check
    }
}
