package com.savlukov.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.savlukov.app.domain.model.ARModelConfig
import com.savlukov.app.domain.model.FurnitureMaterial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ARViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ARModelConfig(modelUrl = ""))
    val uiState: StateFlow<ARModelConfig> = _uiState.asStateFlow()

    fun setModelUrl(url: String) {
        _uiState.update { it.copy(modelUrl = url) }
    }

    /**
     * Updates a specific material in the AR model.
     * @param materialKey The identifier of the material in the GLB file.
     * @param material The new furniture material to apply.
     */
    fun updateMaterial(materialKey: String, material: FurnitureMaterial) {
        _uiState.update { currentState ->
            val newMaterials = currentState.activeMaterials.toMutableMap()
            newMaterials[materialKey] = material
            currentState.copy(activeMaterials = newMaterials)
        }
    }
}
