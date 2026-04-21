package com.savlukov.app.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Catalog : Screen

    @Serializable
    data class ProductDetails(val productId: String) : Screen

    @Serializable
    data class ARViewer(val modelUrl: String) : Screen

    @Serializable
    data class StoryViewer(val storyId: String) : Screen
}
