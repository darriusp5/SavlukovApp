package com.savlukov.app.presentation.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.savlukov.app.presentation.FurnitureViewModel
import com.savlukov.app.presentation.StoriesViewModel
import com.savlukov.app.feature.ar.presentation.ARViewer
import com.savlukov.app.feature.stories.presentation.StoryViewer
import com.savlukov.app.presentation.screens.catalog.FurnitureCatalogScreen
import com.savlukov.app.presentation.screens.detail.FurnitureDetailScreen
import com.savlukov.app.presentation.screens.home.HomeScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavGraph(navController: NavHostController) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home> {
                val storiesViewModel: StoriesViewModel = hiltViewModel()
                val state by storiesViewModel.state.collectAsState()
                
                HomeScreen(
                    stories = state.stories,
                    onStoryClick = { story ->
                        navController.navigate(Screen.StoryViewer(story.id))
                    },
                    onExploreCatalog = {
                        navController.navigate(Screen.Catalog)
                    }
                )
            }

            composable<Screen.Catalog> {
                val viewModel: FurnitureViewModel = hiltViewModel()
                FurnitureCatalogScreen(
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
                val viewModel: FurnitureViewModel = hiltViewModel()
                FurnitureDetailScreen(
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
                ARViewer(
                    modelUrl = route.modelUrl,
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
