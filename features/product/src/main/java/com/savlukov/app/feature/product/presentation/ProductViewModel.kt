package com.savlukov.app.feature.product.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.domain.use_case.GetFurnitureDetailUseCase
import com.savlukov.app.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductState(
    val product: Furniture? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val getFurnitureDetailUseCase: GetFurnitureDetailUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            getFurnitureDetailUseCase(productId).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = ProductState(product = result.data)
                    }
                    is Resource.Error -> {
                        _state.value = ProductState(error = result.message ?: "An unexpected error occurred")
                    }
                    is Resource.Loading -> {
                        _state.value = ProductState(isLoading = true)
                    }
                }
            }
        }
    }
}
