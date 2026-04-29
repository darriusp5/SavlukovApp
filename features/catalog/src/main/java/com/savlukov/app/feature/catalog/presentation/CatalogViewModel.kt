package com.savlukov.app.feature.catalog.presentation

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
data class CatalogState(
    val categories: List<Category> = emptyList(),
    val subcategories: List<Category> = emptyList(),
    val products: List<Furniture> = emptyList(),
    val filteredProducts: List<Furniture> = emptyList(),
    val selectedCategoryId: String? = null,
    val selectedSubcategoryId: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val repository: FurnitureRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CatalogState())
    val state: StateFlow<CatalogState> = _state.asStateFlow()

    init {
        loadData()
        // По умолчанию выбираем "Все" (selectedCategoryId = null)
        _state.value = _state.value.copy(
            selectedCategoryId = null,
            selectedSubcategoryId = null
        )
    }

    private fun loadData() {
        // Load products first - this is the main data
        loadProducts()

        // Load categories in parallel
        viewModelScope.launch {
            repository.getCategories().collect { categoriesResult ->
                when (categoriesResult) {
                    is Resource.Success -> {
                        val allCategories = categoriesResult.data ?: emptyList()
                        val mainCategories = allCategories.filter { it.parentId == null }
                        val subcategories = allCategories.filter { it.parentId != null }

                        // For now, show all main categories - we'll filter them after products are loaded
                        val categoriesWithProducts = mainCategories

                        android.util.Log.d("CatalogViewModel", "Loaded categories: ${mainCategories.size} main, ${subcategories.size} sub")
                        android.util.Log.d("CatalogViewModel", "Categories with products: ${categoriesWithProducts.size}")
                        android.util.Log.d("CatalogViewModel", "Categories with products: ${categoriesWithProducts.joinToString { it.name }}")

                        _state.value = _state.value.copy(
                            categories = categoriesWithProducts,
                            subcategories = subcategories
                        )
                    }
                    is Resource.Error -> {
                        android.util.Log.e("CatalogViewModel", "Failed to load categories: ${categoriesResult.message}")
                        // Don't show error for categories - products are more important
                    }
                    is Resource.Loading -> {
                        // Handle loading if needed
                    }
                }
            }
        }
    }

    private fun loadProducts() {
        android.util.Log.d("CatalogViewModel", "loadProducts() called")
        viewModelScope.launch {
            repository.getCatalog().collect { productsResult ->
                android.util.Log.d("CatalogViewModel", "Received products result: ${productsResult::class.simpleName}")
                when (productsResult) {
                    is Resource.Success -> {
                        val products = productsResult.data ?: emptyList()
                        android.util.Log.d("CatalogViewModel", "Loaded ${products.size} products successfully")
                        if (products.isNotEmpty()) {
                            android.util.Log.d("CatalogViewModel", "Sample: ${products.take(2).joinToString { it.name }}")
                        }
                        // IMPORTANT: Don't overwrite selectedCategoryId and selectedSubcategoryId!
                        _state.value = _state.value.copy(
                            products = products,
                            filteredProducts = emptyList(), // Reset filtered products
                            isLoading = false,
                            error = null
                        )
                        android.util.Log.d("CatalogViewModel", "After loading products: categoryId='${_state.value.selectedCategoryId}', subcategoryId='${_state.value.selectedSubcategoryId}'")
                        applyFilter()
                    }
                    is Resource.Error -> {
                        android.util.Log.e("CatalogViewModel", "Error loading products: ${productsResult.message}")
                        _state.value = _state.value.copy(error = productsResult.message ?: "Failed to load products", isLoading = false)
                    }
                    is Resource.Loading -> {
                        android.util.Log.d("CatalogViewModel", "Loading products...")
                        _state.value = _state.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun selectCategory(categoryId: String?) {
        _state.value = _state.value.copy(
            selectedCategoryId = categoryId,
            selectedSubcategoryId = null // Reset subcategory when main category changes
        )
        applyFilter()
    }

    fun selectMainFilter(categoryId: String?) {
        android.util.Log.d("CatalogViewModel", "selectMainFilter called with categoryId=$categoryId")
        // When selecting main filters (Все, Диваны, Кресла), reset subcategories
        _state.value = _state.value.copy(
            selectedCategoryId = categoryId,
            selectedSubcategoryId = null
        )
        android.util.Log.d("CatalogViewModel", "After selectMainFilter: categoryId=${_state.value.selectedCategoryId}, subcategoryId=${_state.value.selectedSubcategoryId}")
        applyFilter()
    }

    fun selectSubcategory(subcategoryId: String?) {
        _state.value = _state.value.copy(selectedSubcategoryId = subcategoryId)
        applyFilter()
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            if (repository.isFavorite(id)) {
                repository.removeFromFavorites(id)
            } else {
                repository.addToFavorites(id)
            }
        }
    }

    private fun applyFilter() {
        val selectedCategoryId = _state.value.selectedCategoryId
        val selectedSubcategoryId = _state.value.selectedSubcategoryId
        val allProducts = _state.value.products

        android.util.Log.d("CatalogViewModel", "applyFilter() - products: ${allProducts.size}, category: '$selectedCategoryId', subcategory: '$selectedSubcategoryId'")

        val filteredProducts = when {
            selectedSubcategoryId != null && selectedSubcategoryId.isNotEmpty() -> {
                // Show products from specific subcategory
                allProducts.filter { it.categoryId == selectedSubcategoryId }
            }
            selectedCategoryId != null && selectedCategoryId.isNotEmpty() -> {
                // For "Диваны" (id="1") - show all sofa products
                // For "Кресла" (id="6") - show chairs (categoryId=6, but none exist in XML)
                if (selectedCategoryId == "1") {
                    // Диваны - show products from all sofa subcategories
                    val sofaSubcategoryIds = listOf("3", "8") // Hardcoded based on actual XML data
                    android.util.Log.d("CatalogViewModel", "Filtering sofas, subcategory IDs: $sofaSubcategoryIds")
                    allProducts.filter { it.categoryId in sofaSubcategoryIds }
                } else if (selectedCategoryId == "6") {
                    // Кресла - show chairs (categoryId=6, but none exist in current XML data)
                    android.util.Log.d("CatalogViewModel", "Filtering chairs - no chairs in current XML data")
                    allProducts.filter { it.categoryId == "6" }
                } else {
                    allProducts.filter { it.categoryId == selectedCategoryId }
                }
            }
            else -> allProducts // "Все" - show all products
        }

        android.util.Log.d("CatalogViewModel", "Filtered ${allProducts.size} -> ${filteredProducts.size} products")
        _state.value = _state.value.copy(filteredProducts = filteredProducts, isLoading = false, error = null)
    }
}
