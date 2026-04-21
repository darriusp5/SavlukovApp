package com.savlukov.app.presentation.screens.catalog

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.savlukov.app.domain.model.Furniture
import com.savlukov.app.presentation.FurnitureViewModel
import com.savlukov.app.core.ui.SavlukovCard

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FurnitureCatalogScreen(
    viewModel: FurnitureViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onItemClick: (String) -> Unit
) {
    val state by viewModel.catalogState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Collections",
                        style = MaterialTheme.typography.headlineLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (state.error != null) {
                Text(
                    text = state.error ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.furniture, key = { it.id }) { item ->
                    with(sharedTransitionScope) {
                        FurnitureItem(
                            furniture = item,
                            animatedVisibilityScope = animatedVisibilityScope,
                            onClick = { onItemClick(item.id) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FurnitureItem(
    furniture: Furniture,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onClick: () -> Unit
) {
    SavlukovCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            AsyncImage(
                model = furniture.imageUrl,
                contentDescription = furniture.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .sharedElement(
                        rememberSharedContentState(key = "image-${furniture.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Crop
            )
            
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = furniture.name,
                    style = MaterialTheme.typography.headlineMedium,
                    maxLines = 1,
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "name-${furniture.id}"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Experience the Look",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
                )
            }
        }
    }
}
