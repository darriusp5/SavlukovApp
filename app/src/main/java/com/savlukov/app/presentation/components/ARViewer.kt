package com.savlukov.app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.align
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode

/**
 * Premium AR Viewer for Savlukov Furniture.
 */
@Composable
fun ARViewer(
    modelUrl: String,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isModelLoaded by remember { mutableStateOf(false) }
    
    val fabrics = remember {
        listOf(
            Fabric("1", "Onyx Velvet", Color(0xFF1A1A1A)),
            Fabric("2", "Champagne Silk", Color(0xFFD4AF37)),
            Fabric("3", "Royal Burgundy", Color(0xFF4A0E0E)),
            Fabric("4", "Cloud Linen", Color(0xFFF8F8F8))
        )
    }
    var selectedFabric by remember { mutableStateOf(fabrics[0]) }

    val modelNode = remember {
        ArModelNode(placementMode = PlacementMode.INSTANT).apply {
            loadModelGlbAsync(
                glbFileLocation = modelUrl,
                autoAnimate = true,
                scaleToUnits = 1.0f,
                centerOrigin = null
            ) {
                isModelLoaded = true
            }
        }
    }

    // Применяем выбранную ткань к модели
    LaunchedEffect(selectedFabric, isModelLoaded) {
        if (isModelLoaded) {
            modelNode.modelInstance?.materialInstances?.forEach { materialInstance ->
                // Применяем цвет к параметру baseColorFactor
                materialInstance.setParameter(
                    "baseColorFactor",
                    selectedFabric.color.red,
                    selectedFabric.color.green,
                    selectedFabric.color.blue,
                    selectedFabric.color.alpha
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = remember { listOf(modelNode) },
            planeRenderer = true,
            onCreate = { arSceneView ->
                arSceneView.lightEstimationMode = io.google.ar.core.Config.LightEstimationMode.ENVIRONMENTAL_HDR
            }
        )

        // HUD: Close Button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(48.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.Black.copy(alpha = 0.3f),
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close AR")
        }

        // HUD: Snapshot Button (Quiet Luxury style)
        IconButton(
            onClick = { /* Snapshot logic */ },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 24.dp)
                .size(64.dp),
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Color.White.copy(alpha = 0.5f),
                contentColor = Color.Black
            )
        ) {
            Icon(
                Icons.Default.PhotoCamera, 
                contentDescription = "Take Photo",
                modifier = Modifier.size(32.dp)
            )
        }

        // Fabric Switcher
        FabricSelector(
            fabrics = fabrics,
            selectedFabric = selectedFabric,
            onFabricSelected = { selectedFabric = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
