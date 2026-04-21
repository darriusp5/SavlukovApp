package com.savlukov.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class Fabric(
    val id: String,
    val name: String,
    val color: Color,
    val textureUrl: String? = null
)

@Composable
fun FabricSelector(
    fabrics: List<Fabric>,
    selectedFabric: Fabric?,
    onFabricSelected: (Fabric) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.5f))
                )
            )
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedFabric?.let {
            Text(
                text = it.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(fabrics) { fabric ->
                val isSelected = fabric.id == selectedFabric?.id
                FabricSwatch(
                    fabric = fabric,
                    isSelected = isSelected,
                    onClick = { onFabricSelected(fabric) }
                )
            }
        }
    }
}

@Composable
fun FabricSwatch(
    fabric: Fabric,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable { onClick() }
            .border(
                border = if (isSelected) {
                    BorderStroke(2.dp, Color(0xFFD4AF37)) // Champagne Gold
                } else {
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
                },
                shape = RoundedCornerShape(28.dp)
            )
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(24.dp))
                .background(fabric.color)
        ) {
            if (fabric.textureUrl != null) {
                AsyncImage(
                    model = fabric.textureUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
