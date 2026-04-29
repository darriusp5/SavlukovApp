package com.savlukov.app.feature.catalog.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import androidx.compose.runtime.LaunchedEffect
import com.savlukov.app.domain.model.Furniture

@Composable
fun ProductPreviewCard(
    product: Furniture,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color.LightGray, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Discount badge
                product.discountPercent?.let { discount ->
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text("-$discount%", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // Product Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Category
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                product.price?.let { price ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "${price.toInt()} BYN",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            product.oldPrice?.let { oldPrice ->
                                Text(
                                    text = "${oldPrice.toInt()} BYN",
                                    style = MaterialTheme.typography.bodySmall,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ModernProductCard(
    product: Furniture,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Heart button overlay
                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(36.dp)
                        .background(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Toggle favorite",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Discount badge
                product.discountPercent?.let { discount ->
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text("-$discount%", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }

            // Product Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Category
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )

                // Price Section
                product.price?.let { price ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                text = "${price.toInt()} BYN",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            product.oldPrice?.let { oldPrice ->
                                Text(
                                    text = "${oldPrice.toInt()} BYN",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textDecoration = TextDecoration.LineThrough,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

                // Dimensions
                if (product.length != null || product.width != null || product.height != null) {
                    Text(
                        text = "📏 Габариты:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        product.length?.let { Text("• Длина: ${it} мм", style = MaterialTheme.typography.bodySmall) }
                        product.width?.let { Text("• Ширина: ${it} мм", style = MaterialTheme.typography.bodySmall) }
                        product.height?.let { Text("• Высота: ${it} мм", style = MaterialTheme.typography.bodySmall) }
                    }
                }

                // Sleeping place dimensions
                if (product.bedLength != null || product.bedWidth != null) {
                    Text(
                        text = "🛏️ Спальное место:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        product.bedLength?.let { Text("• Длина: ${it} мм", style = MaterialTheme.typography.bodySmall) }
                        product.bedWidth?.let { Text("• Ширина: ${it} мм", style = MaterialTheme.typography.bodySmall) }
                    }
                }

                // Technical characteristics
                val hasTechSpecs = product.frame != null || product.upholstery != null ||
                                 product.mechanism != null || product.storageBoxes != null

                if (hasTechSpecs) {
                    Text(
                        text = "🔧 Характеристики:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        product.frame?.let { Text("• Каркас: $it", style = MaterialTheme.typography.bodySmall) }
                        product.upholstery?.let { Text("• Наполнение: $it", style = MaterialTheme.typography.bodySmall) }
                        product.mechanism?.let { Text("• Механизм: $it", style = MaterialTheme.typography.bodySmall) }
                        product.storageBoxes?.let { Text("• Ящики для белья: $it", style = MaterialTheme.typography.bodySmall) }
                        product.removableCover?.let { Text("• Чехол: ${if (it == "Да") "Съемный" else "Несъемный"}", style = MaterialTheme.typography.bodySmall) }
                        product.maxLoad?.let { Text("• Макс. нагрузка: $it кг", style = MaterialTheme.typography.bodySmall) }
                    }
                }

                // Design
                val hasDesign = product.fabric != null || product.style != null || product.seatsCount != null

                if (hasDesign) {
                    Text(
                        text = "🎨 Дизайн:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        product.fabric?.let { Text("• Ткань: $it", style = MaterialTheme.typography.bodySmall) }
                        product.style?.let { Text("• Стиль: $it", style = MaterialTheme.typography.bodySmall) }
                        product.seatsCount?.let { Text("• Посадочных мест: $it", style = MaterialTheme.typography.bodySmall) }
                    }
                }

                // Manufacturer
                product.manufacturer?.let { manufacturer ->
                    Text(
                        text = "🏭 Производство: $manufacturer",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action button
                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text(
                        text = "ПОДРОБНЕЕ",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel,
    onItemClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    android.util.Log.d("CatalogScreen", "=== CATALOG SCREEN INIT ===")
    android.util.Log.d("CatalogScreen", "State: categoryId='${state.selectedCategoryId}', subcategoryId='${state.selectedSubcategoryId}', allSelected=${state.selectedCategoryId == null && state.selectedSubcategoryId == null}")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with title and search
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Каталог",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = (-1).sp
                )
            )
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            }
        }

        // Main Category Filter
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                FilterChip(
                    selected = state.selectedCategoryId == null && state.selectedSubcategoryId == null,
                    onClick = { viewModel.selectMainFilter(null) },
                    label = { Text("Все", style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            // Hardcoded main categories: Диваны and Кресла
            item {
                FilterChip(
                    selected = state.selectedCategoryId == "1" && state.selectedSubcategoryId == null,
                    onClick = { viewModel.selectMainFilter("1") }, // Диваны
                    label = { Text("Диваны", style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
            item {
                FilterChip(
                    selected = state.selectedCategoryId == "6",
                    onClick = { viewModel.selectMainFilter("6") }, // Кресла
                    label = { Text("Кресла", style = MaterialTheme.typography.labelMedium) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        // Subcategory Filter (only show if main category is selected)
        if (state.selectedCategoryId != null) {
            val availableSubcategories = state.subcategories
                .filter { it.parentId == state.selectedCategoryId }
                .filter { subcategory ->
                    // Only show subcategories that have products
                    state.products.any { it.categoryId == subcategory.id }
                }
                .sortedBy { it.name } // Sort alphabetically

            if (availableSubcategories.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedSubcategoryId == null,
                            onClick = { viewModel.selectSubcategory(null) },
                            label = { Text("Все ${state.categories.find { it.id == state.selectedCategoryId }?.name?.lowercase() ?: ""}", style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                    items(availableSubcategories) { subcategory ->
                        FilterChip(
                            selected = state.selectedSubcategoryId == subcategory.id,
                            onClick = { viewModel.selectSubcategory(subcategory.id) },
                            label = { Text(subcategory.name, style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
                            )
                        )
                    }
                }
            }
        }

        // Product Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Две колонки для preview карточек
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(state.filteredProducts) { product ->
                ProductPreviewCard(
                    product = product,
                    onClick = { onItemClick(product.id) },
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}


