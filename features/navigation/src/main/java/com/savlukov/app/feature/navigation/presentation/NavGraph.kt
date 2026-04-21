package com.savlukov.app.feature.navigation.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.savlukov.app.feature.ar.presentation.ARViewer
import com.savlukov.app.feature.ar.presentation.ARViewModel
import com.savlukov.app.feature.stories.presentation.StoriesViewModel
import com.savlukov.app.feature.stories.presentation.StoryViewer
import com.savlukov.app.feature.catalog.presentation.CatalogScreen
import com.savlukov.app.feature.catalog.presentation.CatalogViewModel
import com.savlukov.app.feature.product.presentation.ProductDetailScreen
import com.savlukov.app.feature.product.presentation.ProductViewModel
import com.savlukov.app.feature.home.presentation.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                val storiesViewModel: StoriesViewModel = hiltViewModel()
                val state by storiesViewModel.state.collectAsStateWithLifecycle()
                
                HomeScreen(
                    stories = state.stories,
                    onStoryClick = { storyId ->
                        navController.navigate(Screen.StoryViewer(storyId))
                    },
                    onExploreCatalog = {
                        navController.navigate(Screen.Catalog)
                    }
                )
            }

            composable<Screen.Catalog> {
                val viewModel: CatalogViewModel = hiltViewModel()
                CatalogScreen(
                    viewModel = viewModel,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onItemClick = { productId ->
                        navController.navigate(Screen.ProductDetails(productId))
                    }
                )
            }

            composable<Screen.ProductDetails> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.ProductDetails>()
                val viewModel: ProductViewModel = hiltViewModel()
                ProductDetailScreen(
                    id = route.productId,
                    viewModel = viewModel,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable,
                    onBackClick = { navController.popBackStack() },
                    onARClick = { modelUrl ->
                        navController.navigate(Screen.ARViewer(modelUrl))
                    }
                )
            }

            composable<Screen.ARViewer> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.ARViewer>()
                val arViewModel: ARViewModel = hiltViewModel()
                ARViewer(
                    modelUrl = route.modelUrl,
                    viewModel = arViewModel,
                    onClose = { navController.popBackStack() }
                )
            }

            composable<Screen.StoryViewer> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.StoryViewer>()
                val storiesViewModel: StoriesViewModel = hiltViewModel()
                StoryViewer(
                    viewModel = storiesViewModel,
                    onClose = { navController.popBackStack() }
                )
            }
        }
    }
}
