package com.savlukov.app.presentation

import app.cash.turbine.test
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.usecase.GetFurnitureDetailUseCase
import com.savlukov.app.domain.usecase.GetFurnitureUseCase
import com.savlukov.app.domain.util.Resource
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FurnitureViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getFurnitureUseCase: GetFurnitureUseCase
    private lateinit var getFurnitureDetailUseCase: GetFurnitureDetailUseCase
    private lateinit var viewModel: FurnitureViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getFurnitureUseCase = mockk()
        getFurnitureDetailUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadCatalog sets Loading then Success state`() = runTest {
        // Arrange
        val furnitureList = listOf(
            Furniture("1", "Sofa", "Desc", "Cat", 100.0, "img", "ar")
        )
        every { getFurnitureUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Success(furnitureList)
        )

        // Act
        viewModel = FurnitureViewModel(getFurnitureUseCase, getFurnitureDetailUseCase)

        // Assert
        viewModel.catalogState.test {
            val item1 = awaitItem() // Initial state or Loading
            if (item1.isLoading) {
                val item2 = awaitItem()
                assertEquals(furnitureList, item2.furniture)
                assertEquals(false, item2.isLoading)
            } else {
                // If it emitted Loading first
                val item2 = awaitItem() // Loading
                assert(item2.isLoading)
                val item3 = awaitItem() // Success
                assertEquals(furnitureList, item3.furniture)
                assertEquals(false, item3.isLoading)
            }
        }
    }

    @Test
    fun `loadCatalog sets Loading then Error state`() = runTest {
        // Arrange
        val errorMsg = "Error loading"
        every { getFurnitureUseCase() } returns flowOf(
            Resource.Loading,
            Resource.Error(errorMsg)
        )

        // Act
        viewModel = FurnitureViewModel(getFurnitureUseCase, getFurnitureDetailUseCase)

        // Assert
        viewModel.catalogState.test {
            val loadingState = awaitItem() 
            // In case of immediate emissions, we handle both possibilities
            if (loadingState.isLoading) {
                val errorState = awaitItem()
                assertEquals(errorMsg, errorState.error)
                assertEquals(false, errorState.isLoading)
            } else {
                val nextLoadingState = awaitItem()
                assert(nextLoadingState.isLoading)
                val errorState = awaitItem()
                assertEquals(errorMsg, errorState.error)
                assertEquals(false, errorState.isLoading)
            }
        }
    }

    @Test
    fun `loadFurnitureDetail sets Loading then Success state`() = runTest {
        // Arrange
        val furnitureId = "1"
        val furniture = Furniture(furnitureId, "Sofa", "Desc", "Cat", 100.0, "img", "ar")
        every { getFurnitureUseCase() } returns flowOf(Resource.Loading)
        every { getFurnitureDetailUseCase(furnitureId) } returns flowOf(
            Resource.Loading,
            Resource.Success(furniture)
        )

        viewModel = FurnitureViewModel(getFurnitureUseCase, getFurnitureDetailUseCase)

        // Act
        viewModel.loadFurnitureDetail(furnitureId)

        // Assert
        viewModel.detailState.test {
            val initialState = awaitItem()
            assertEquals(null, initialState.item)
            
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            
            val successState = awaitItem()
            assertEquals(furniture, successState.item)
            assertEquals(false, successState.isLoading)
        }
    }
}
