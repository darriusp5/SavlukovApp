package com.savlukov.app.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savlukov.app.domain.model.Story
import com.savlukov.app.feature.stories.presentation.BrandStoriesBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    stories: List<Story>,
    onStoryClick: (String) -> Unit,
    onExploreCatalog: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SAVLUCOV",
                        style = MaterialTheme.typography.headlineMedium,
                        letterSpacing = 4.sp
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Stories Bar
            BrandStoriesBar(
                stories = stories,
                onStoryClick = { story -> onStoryClick(story.id) }
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Hero Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Crafted for Generations.",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onExploreCatalog,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Text(text = "Explore Collections")
            }
        }
    }
}
