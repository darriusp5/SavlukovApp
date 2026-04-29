package com.savlukov.app.feature.ar.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.savlukov.app.domain.model.Fabric

@Composable
fun FabricSelector(
    fabrics: List<Fabric>,
    onFabricSelected: (Fabric) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFabricId by remember { mutableStateOf(fabrics.firstOrNull()?.id) }
    val selectedFabric = fabrics.find { it.id == selectedFabricId }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.6f))
                )
            )
            .navigationBarsPadding()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        selectedFabric?.let {
            Text(
                text = it.name.uppercase(),
                color = Color.White,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(fabrics) { fabric ->
                val isSelected = fabric.id == selectedFabricId
                FabricSwatch(
                    fabric = fabric,
                    isSelected = isSelected,
                    onClick = { 
                        selectedFabricId = fabric.id
                        onFabricSelected(fabric) 
                    }
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
    val color = remember(fabric.colorHex) {
        try {
            Color(android.graphics.Color.parseColor(fabric.colorHex ?: "#FFFFFF"))
        } catch (e: Exception) {
            Color.White
        }
    }

    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(30.dp))
            .clickable { onClick() }
            .border(
                border = if (isSelected) {
                    BorderStroke(2.dp, Color(0xFFD4AF37)) // Champagne Gold
                } else {
                    BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                },
                shape = RoundedCornerShape(30.dp)
            )
            .padding(if (isSelected) 4.dp else 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(30.dp))
                .background(color)
        ) {
            if (fabric.thumbnail.startsWith("http")) {
                AsyncImage(
                    model = fabric.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
