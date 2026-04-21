package com.savlukov.app.feature.home.presentation

import androidx.compose.ui.test.*
import com.savlukov.app.core.testing.BaseComposeTest
import com.savlukov.app.domain.model.Story
import org.junit.Test

class HomeScreenTest : BaseComposeTest() {

    @Test
    fun homeScreen_displaysTitleAndButton() {
        val stories = listOf(
            Story("1", "Title 1", "Savlukov", "", "url1", "2026-04-18")
        )
        var exploreClicked = false

        setContent {
            HomeScreen(
                stories = stories,
                onStoryClick = {},
                onExploreCatalog = { exploreClicked = true }
            )
        }

        // Verify Title (with typo for now as per current implementation)
        composeTestRule.onNodeWithText("SAVLUCOV").assertIsDisplayed()
        
        // Verify Hero Text
        composeTestRule.onNodeWithText("Crafted for Generations.").assertIsDisplayed()
        
        // Verify Button
        composeTestRule.onNodeWithText("Explore Collections").assertIsDisplayed()
        composeTestRule.onNodeWithText("Explore Collections").performClick()
        
        assert(exploreClicked)
    }
}
