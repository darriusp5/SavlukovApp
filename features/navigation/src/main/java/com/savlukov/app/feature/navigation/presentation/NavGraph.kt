package com.savlukov.app.feature.navigation.presentation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.savlukov.app.domain.navigation.Screen
import com.savlukov.app.feature.about.presentation.AboutScreen
import com.savlukov.app.feature.ar.presentation.ArtParksScreen
import com.savlukov.app.feature.catalog.presentation.CatalogScreen
import com.savlukov.app.feature.catalog.presentation.CatalogViewModel
import com.savlukov.app.feature.catalog.presentation.FavoritesScreen
import com.savlukov.app.feature.catalog.presentation.FavoritesViewModel
import com.savlukov.app.feature.delivery.presentation.DeliveryScreen
import com.savlukov.app.feature.home.presentation.HomeScreen
import com.savlukov.app.feature.home.presentation.HomeViewModel
import com.savlukov.app.feature.product.presentation.ProductDetailScreen
import com.savlukov.app.feature.product.presentation.ProductViewModel
import com.savlukov.app.feature.search.presentation.SearchScreen
import com.savlukov.app.feature.stories.presentation.StoriesViewModel
import com.savlukov.app.feature.stories.presentation.StoryViewer

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeRoute,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() }
    ) {
        composable(Screen.HomeRoute) {
            val storiesViewModel: StoriesViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()

            HomeScreen(
                viewModel = homeViewModel,
                onStoryClick = { storyId ->
                    navController.navigate("${Screen.StoryViewerRoute}/$storyId")
                },
                onExploreCatalog = {
                    navController.navigate(Screen.CatalogRoute)
                },
                onCategoryClick = { categoryId ->
                    navController.navigate("${Screen.CatalogRoute}?category=$categoryId")
                },
                onProductClick = { productId ->
                    navController.navigate("${Screen.ProductDetailsRoute}/$productId")
                },
                onSearchClick = {
                    navController.navigate(Screen.SearchRoute)
                }
            )
        }

        composable(
            route = "${Screen.CatalogRoute}?category={category}",
            arguments = listOf(navArgument("category") { type = NavType.StringType; defaultValue = "" })
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("category") ?: ""
            val viewModel: CatalogViewModel = hiltViewModel()
            // Select category if provided
            LaunchedEffect(categoryId) {
                viewModel.selectCategory(categoryId)
            }
            CatalogScreen(
                viewModel = viewModel,
                onItemClick = { productId ->
                    navController.navigate("${Screen.ProductDetailsRoute}/$productId")
                },
                onSearchClick = {
                    navController.navigate(Screen.SearchRoute)
                }
            )
        }

        composable("${Screen.ProductDetailsRoute}/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            val viewModel: ProductViewModel = hiltViewModel()
            ProductDetailScreen(
                id = productId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() },
                onToggleFavorite = {
                    // Toggle favorite implemented via DB Room
                }
            )
        }

        composable(Screen.SearchRoute) {
            val catalogViewModel: CatalogViewModel = hiltViewModel()
            val catalogState by catalogViewModel.state.collectAsStateWithLifecycle()

            SearchScreen(
                products = catalogState.products,
                onProductClick = { productId ->
                    navController.navigate("${Screen.ProductDetailsRoute}/$productId")
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.AboutRoute) {
            AboutScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.ArtParksRoute) {
            ArtParksScreen(onBackClick = { navController.popBackStack() })
        }

        composable(Screen.FavoritesRoute) {
            val favoritesViewModel: FavoritesViewModel = hiltViewModel()
            FavoritesScreen(
                viewModel = favoritesViewModel,
                onItemClick = { productId ->
                    navController.navigate("${Screen.ProductDetailsRoute}/$productId")
                }
            )
        }

        composable("${Screen.StoryViewerRoute}/{storyId}") { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            val storiesViewModel: StoriesViewModel = hiltViewModel()
            StoryViewer(
                viewModel = storiesViewModel,
                onClose = { navController.popBackStack() }
            )
        }
    }
}
