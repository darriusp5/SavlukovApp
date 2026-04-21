package com.savlukov.app.feature.home.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.savlukov.app.domain.model.Story
import com.savlukov.app.feature.stories.presentation.BrandStoriesBar
import com.savlukov.app.presentation.common.SavlukovButton

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
                        letterSpacing = 8.sp,
                        fontWeight = FontWeight.Light
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
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Hero Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Text(
                    text = "CRAFTED FOR\nGENERATIONS.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        lineHeight = 48.sp,
                        letterSpacing = (-1).sp
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "A legacy of Belarusian craftsmanship, reimagined for the modern home.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            SavlukovButton(
                onClick = onExploreCatalog,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
                    .height(64.dp)
            ) {
                Text(
                    text = "EXPLORE COLLECTIONS",
                    style = MaterialTheme.typography.labelLarge,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}
