package com.savlukov.app.feature.home.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Category
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.repository.FurnitureRepository
import com.savlukov.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@Immutable
data class HomeState(
    val categories: List<Category> = emptyList(),
    val bestsellers: List<Furniture> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Load categories
            repository.getCategories().collect { categoriesResult ->
                when (categoriesResult) {
                    is Resource.Success -> {
                        val categories = categoriesResult.data ?: emptyList()
                        _state.value = _state.value.copy(categories = categories.filter { it.parentId == null })
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = categoriesResult.message ?: "Failed to load categories")
                    }
                    is Resource.Loading -> {
                        // Handle loading if needed
                    }
                }
            }
        }

        viewModelScope.launch {
            // Load bestsellers (first 6 products)
            repository.getCatalog().collect { productsResult ->
                when (productsResult) {
                    is Resource.Success -> {
                        val products = productsResult.data ?: emptyList()
                        _state.value = _state.value.copy(bestsellers = products.take(6))
                    }
                    is Resource.Error -> {
                        _state.value = _state.value.copy(error = productsResult.message ?: "Failed to load products")
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}