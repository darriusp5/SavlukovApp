package com.savlukov.app.feature.catalog.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.use_case.GetCatalogUseCase
import com.savlukov.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class CatalogState(
    val products: List<Furniture> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val getCatalogUseCase: GetCatalogUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    init {
        loadCatalog()
    }

    fun loadCatalog() {
        viewModelScope.launch {
            getCatalogUseCase(Unit).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = CatalogState(products = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = CatalogState(error = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _state.value = CatalogState(isLoading = true)
                    }
                }
            }
        }
    }
}
