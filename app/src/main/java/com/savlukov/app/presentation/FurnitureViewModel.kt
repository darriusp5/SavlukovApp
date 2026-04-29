package com.savlukov.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.use_case.GetCatalogUseCase
import com.savlukov.app.domain.use_case.GetFurnitureDetailUseCase
import com.savlukov.app.domain.use_case.GetFabricsUseCase
import com.savlukov.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CatalogState(
    val furniture: List<Furniture> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class DetailState(
    val item: Furniture? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

data class FabricState(
    val fabrics: List<Fabric> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FurnitureViewModel @Inject constructor(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val getFurnitureDetailUseCase: GetFurnitureDetailUseCase,
    private val getFabricsUseCase: GetFabricsUseCase
) : ViewModel() {

    private val _catalogState = MutableStateFlow(CatalogState())
    val catalogState: StateFlow<CatalogState> = _catalogState.asStateFlow()

    private val _detailState = MutableStateFlow(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    private val _fabricState = MutableStateFlow(FabricState())
    val fabricState: StateFlow<FabricState> = _fabricState.asStateFlow()

    init {
        loadCatalog()
        loadFabrics()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            getCatalogUseCase(Unit).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _catalogState.value = CatalogState(furniture = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _catalogState.value = CatalogState(error = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _catalogState.value = CatalogState(isLoading = true)
                    }
                }
            }
        }
    }

    fun loadFurnitureDetail(id: String) {
        viewModelScope.launch {
            getFurnitureDetailUseCase(id).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _detailState.value = DetailState(item = result.data)
                    }
                    is Resource.Error -> {
                        _detailState.value = DetailState(error = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _detailState.value = DetailState(isLoading = true)
                    }
                }
            }
        }
    }

    fun loadFabrics() {
        viewModelScope.launch {
            getFabricsUseCase(Unit).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _fabricState.value = FabricState(fabrics = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _fabricState.value = FabricState(error = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _fabricState.value = FabricState(isLoading = true)
                    }
                }
            }
        }
    }
}
