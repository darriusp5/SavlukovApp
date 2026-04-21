package com.savlukov.app.feature.ar.presentation

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import com.savlukov.app.domain.model.Fabric
import com.savlukov.app.domain.model.FurnitureMaterial

/**
 * Premium AR Viewer for Savlukov Furniture.
 * Optimized for real-time material swapping and high-fidelity visualization.
 */
@Composable
fun ARViewer(
    modelUrl: String,
    viewModel: ARViewModel,
    onClose: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isModelLoaded by remember { mutableStateOf(false) }
    
    // Demo fabrics
    val fabrics = remember {
        listOf(
            Fabric("1", "Royal Velvet - Night", "thumb1", colorHex = "#1A1A1A"),
            Fabric("2", "Champagne Silk", "thumb2", colorHex = "#D4AF37"),
            Fabric("3", "Imperial Burgundy", "thumb3", colorHex = "#4A0E0E"),
            Fabric("4", "Nordic Linen", "thumb4", colorHex = "#F8F8F8")
        )
    }
    
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

    LaunchedEffect(uiState.activeMaterials, isModelLoaded) {
        if (isModelLoaded) {
            val modelInstance = modelNode.modelInstance ?: return@LaunchedEffect
            
            uiState.activeMaterials.forEach { (materialKey, material) ->
                modelInstance.materialInstances.forEach { materialInstance ->
                    val name = materialInstance.name
                    if (materialKey == "all" || name?.contains(materialKey, ignoreCase = true) == true) {
                        applyMaterialToInstance(materialInstance, material)
                    }
                }
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

        IconButton(
            onClick = { /* Implement snapshot capture */ },
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
                Icons.Default.AddAPhoto, 
                contentDescription = "Capture Moment",
                modifier = Modifier.size(32.dp)
            )
        }

        FabricSelector(
            fabrics = fabrics,
            onFabricSelected = { fabric ->
                viewModel.updateMaterial(
                    materialKey = "all",
                    material = FurnitureMaterial(
                        id = fabric.id,
                        name = fabric.name,
                        colorHex = fabric.colorHex
                    )
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}

private fun applyMaterialToInstance(
    instance: io.github.sceneview.material.MaterialInstance,
    material: FurnitureMaterial
) {
    material.colorHex?.let { hex ->
        val color = Color(android.graphics.Color.parseColor(hex))
        instance.setParameter(
            "baseColorFactor",
            color.red,
            color.green,
            color.blue,
            color.alpha
        )
        
        if (material.name.contains("Silk", ignoreCase = true)) {
            instance.setParameter("roughnessFactor", 0.2f)
            instance.setParameter("metallicFactor", 0.1f)
        } else {
            instance.setParameter("roughnessFactor", 0.8f)
            instance.setParameter("metallicFactor", 0.0f)
        }
    }
}
