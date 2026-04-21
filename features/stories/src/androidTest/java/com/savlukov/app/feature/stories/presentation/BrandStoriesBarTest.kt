package com.savlukov.app.feature.stories.presentation

import androidx.compose.ui.test.*
import com.savlukov.app.core.testing.BaseComposeTest
import com.savlukov.app.domain.model.Story
import org.junit.Test

class BrandStoriesBarTest : BaseComposeTest() {

    @Test
    fun brandStoriesBar_displaysStoriesAndTriggersClick() {
        val stories = listOf(
            Story("1", "Story 1", "Brand A", "", "url1", "2026-04-18"),
            Story("2", "Story 2", "Brand B", "", "url2", "2026-04-18")
        )
        var clickedStory: Story? = null

        setContent {
            BrandStoriesBar(
                stories = stories,
                onStoryClick = { clickedStory = it }
            )
        }

        // Verify Brand Names are displayed
        composeTestRule.onNodeWithText("Brand A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Brand B").assertIsDisplayed()
        
        // Test interaction
        composeTestRule.onNodeWithText("Brand A").performClick()
        
        assert(clickedStory?.id == "1")
    }
}
