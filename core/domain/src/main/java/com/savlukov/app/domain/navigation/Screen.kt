package com.savlukov.app.domain.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Catalog : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data object About : Screen()

    @Serializable
    data object ArtParks : Screen()

    @Serializable
    data object Favorites : Screen()

    @Serializable
    data class ProductDetails(val productId: String) : Screen()

    @Serializable
    data class StoryViewer(val storyId: String) : Screen()

    companion object {
        const val HomeRoute = "home"
        const val CatalogRoute = "catalog"
        const val SearchRoute = "search"
        const val AboutRoute = "about"
        const val ArtParksRoute = "artparks"
        const val FavoritesRoute = "favorites"
        const val ProductDetailsRoute = "product_details"
        const val StoryViewerRoute = "story_viewer"
    }
}