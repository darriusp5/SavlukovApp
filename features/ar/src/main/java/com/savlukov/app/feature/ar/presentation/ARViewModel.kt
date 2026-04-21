package com.savlukov.app.feature.ar.presentation

import androidx.lifecycle.ViewModel
import com.savlukov.app.domain.model.ARModelConfig
import com.savlukov.app.domain.model.FurnitureMaterial
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ARModelConfig(modelUrl = ""))
    val uiState: StateFlow<ARModelConfig> = _uiState.asStateFlow()

    fun setModelUrl(url: String) {
        _uiState.update { it.copy(modelUrl = url) }
    }

    fun updateMaterial(materialKey: String, material: FurnitureMaterial) {
        _uiState.update { currentState ->
            val newMaterials = currentState.activeMaterials.toMutableMap()
            newMaterials[materialKey] = material
            currentState.copy(activeMaterials = newMaterials)
        }
    }
}
