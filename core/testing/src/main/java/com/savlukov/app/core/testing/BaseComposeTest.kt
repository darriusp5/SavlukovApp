package com.savlukov.app.core.testing

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule

/**
 * Base class for all Compose UI tests.
 * Provides a [ComposeTestRule].
 */
abstract class BaseComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Sets the content for testing.
     */
    fun setContent(
        content: @Composable () -> Unit
    ) {
        composeTestRule.setContent {
            content()
        }
    }
}
