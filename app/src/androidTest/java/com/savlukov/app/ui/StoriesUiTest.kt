package com.savlukov.app.ui

import androidx.compose.ui.test.*
import com.savlukov.app.base.BaseComposeTest
import com.savlukov.app.data.repository.MockStoriesRepository
import com.savlukov.app.feature.stories.presentation.StoriesViewModel
import com.savlukov.app.feature.stories.presentation.StoryViewer
import kotlinx.coroutines.test.runTest
import org.junit.Test

class StoriesUiTest : BaseComposeTest() {

    @Test
    fun storyViewer_displaysBrandAndSegments() = runTest {
        val repository = MockStoriesRepository()
        val viewModel = StoriesViewModel(repository)
        
        // Wait for loading to finish (mock repo has 400ms delay)
        // In a real test we'd use idling resources or advance time
        
        setSavlukovContent {
            StoryViewer(
                viewModel = viewModel,
                onClose = {}
            )
        }

        // Check for brand name
        composeTestRule.onNodeWithText("Savlukov").assertIsDisplayed()
        
        // Check for CTA
        composeTestRule.onNodeWithText("Experience the Look").assertIsDisplayed()
        
        // Test navigation interaction (tap right side to go to next segment)
        composeTestRule.onRoot().performTouchInput {
            click(percentOffset(0.8f, 0.5f))
        }
        
        // Should still show Experience the Look on the next segment
        composeTestRule.onNodeWithText("Experience the Look").assertIsDisplayed()
    }
}
