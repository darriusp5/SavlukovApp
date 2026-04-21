package com.savlukov.app.presentation.viewmodel

import app.cash.turbine.test
import com.savlukov.app.domain.model.ARModelConfig
import com.savlukov.app.domain.model.FurnitureMaterial
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ARViewModelTest {

    @Test
    fun `setModelUrl updates the state with the correct URL`() = runTest {
        val viewModel = ARViewModel()
        val testUrl = "https://example.com/model.glb"

        viewModel.setModelUrl(testUrl)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(testUrl, state.modelUrl)
        }
    }

    @Test
    fun `updateMaterial adds a material to activeMaterials map`() = runTest {
        val viewModel = ARViewModel()
        val materialKey = "fabric_main"
        val material = FurnitureMaterial(
            id = "velvet_red",
            name = "Red Velvet",
            colorHex = "#FF0000"
        )

        viewModel.updateMaterial(materialKey, material)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(material, state.activeMaterials[materialKey])
        }
    }

    @Test
    fun `updateMaterial replaces an existing material in activeMaterials map`() = runTest {
        val viewModel = ARViewModel()
        val materialKey = "fabric_main"
        val material1 = FurnitureMaterial(id = "1", name = "Old")
        val material2 = FurnitureMaterial(id = "2", name = "New")

        viewModel.updateMaterial(materialKey, material1)
        viewModel.updateMaterial(materialKey, material2)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(material2, state.activeMaterials[materialKey])
        }
    }
}
