package com.savlukov.app.feature.product.presentation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage

import com.savlukov.app.core.ui.SavlukovButton

@Composable
fun ProductDetailScreen(
    id: String,
    viewModel: ProductViewModel,
    onBackClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadProduct(id)
    }

    val product = state.product

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (product != null) {
            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Product Image Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color.LightGray)
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
                                .padding(16.dp)
                        ) {
                            Text("-$discount%", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }

                // Product Info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Category badge
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = product.category.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Name
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Price Section
                    product.price?.let { price ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                product.oldPrice?.let { oldPrice ->
                                    if (oldPrice > 0) {
                                        Text(
                                            text = "${oldPrice.toInt()} BYN",
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                    }
                                }
                                Text(
                                    text = "${price.toInt()} BYN",
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Favorite button
                            IconButton(
                                onClick = { onToggleFavorite() },
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.surface,
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Toggle favorite",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(24.dp))

                    // Specifications sections
                    product.let { p ->
                        // Dimensions
                        if (p.length != null || p.width != null || p.height != null) {
                            SpecificationSection("📏 ГАБАРИТЫ") {
                                p.length?.let { SpecificationRow("Длина", "${it} мм") }
                                p.width?.let { SpecificationRow("Ширина", "${it} мм") }
                                p.height?.let { SpecificationRow("Высота", "${it} мм") }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Sleeping place
                        if (p.bedLength != null || p.bedWidth != null) {
                            SpecificationSection("🛏️ СПАЛЬНОЕ МЕСТО") {
                                p.bedLength?.let { SpecificationRow("Длина спального места", "${it} мм") }
                                p.bedWidth?.let { SpecificationRow("Ширина спального места", "${it} мм") }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Technical specifications
                        val hasTechSpecs = p.frame != null || p.upholstery != null ||
                                          p.legs != null || p.mechanism != null || p.maxLoad != null ||
                                          p.storageBoxes != null || p.removableCover != null

                        if (hasTechSpecs) {
                            SpecificationSection("🔧 ТЕХНИЧЕСКИЕ ХАРАКТЕРИСТИКИ") {
                                p.frame?.let { SpecificationRow("Каркас", it) }
                                p.upholstery?.let { SpecificationRow("Наполнение подушек", it) }
                                p.legs?.let { SpecificationRow("Материал опор", it) }
                                p.mechanism?.let { SpecificationRow("Механизм трансформации", it) }
                                p.maxLoad?.let { SpecificationRow("Макс. нагрузка", "${it} кг") }
                                p.storageBoxes?.let { SpecificationRow("Ящик для белья", it) }
                                p.removableCover?.let { SpecificationRow("Съемный чехол", it) }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Design
                        val hasDesign = p.fabric != null || p.style != null || p.seatsCount != null

                        if (hasDesign) {
                            SpecificationSection("🎨 ДИЗАЙН") {
                                p.fabric?.let { SpecificationRow("Ткань", it) }
                                p.style?.let { SpecificationRow("Стиль", it) }
                                p.seatsCount?.let { SpecificationRow("Количество посадочных мест", it) }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Manufacturer
                        p.manufacturer?.let {
                            SpecificationSection("🏭 ПРОИЗВОДСТВО") {
                                SpecificationRow("Производитель", it)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        } else if (state.error != null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.error ?: "Error loading product",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.loadProduct(id) }) {
                    Text("Повторить")
                }
            }
        }

        // Top Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(24.dp))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}

@Composable
fun SpecificationSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
fun SpecificationRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = androidx.compose.ui.text.style.TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}
