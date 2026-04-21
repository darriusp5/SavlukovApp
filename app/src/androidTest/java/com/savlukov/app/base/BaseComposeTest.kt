package com.savlukov.app.base

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import com.savlukov.app.presentation.theme.SavlukovTheme
import org.junit.Rule

/**
 * Base class for all Compose UI tests in the Savlukov application.
 * Provides a [ComposeTestRule] and a helper method to set content with the application theme.
 */
abstract class BaseComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Sets the given [content] wrapped in [SavlukovTheme].
     */
    fun setSavlukovContent(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit
    ) {
        composeTestRule.setContent {
            SavlukovTheme(darkTheme = darkTheme) {
                content()
            }
        }
    }
}
