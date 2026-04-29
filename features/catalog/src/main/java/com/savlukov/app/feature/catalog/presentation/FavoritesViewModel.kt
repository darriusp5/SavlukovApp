package com.savlukov.app.feature.catalog.presentation

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
data class FavoritesState(
    val favorites: List<Furniture> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _state.value = FavoritesState(favorites = result.data ?: emptyList())
                    }
                    is Resource.Error -> {
                        _state.value = FavoritesState(error = result.message ?: "Failed to load favorites")
                    }
                    is Resource.Loading -> {
                        _state.value = FavoritesState(isLoading = true)
                    }
                }
            }
        }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            if (repository.isFavorite(id)) {
                repository.removeFromFavorites(id)
            } else {
                repository.addToFavorites(id)
            }
            loadFavorites() // Reload to update UI
        }
    }
}