package com.savlukov.app.core.ui

import androidx.compose.material3.Text
import androidx.compose.ui.test.*
import com.savlukov.app.base.BaseComposeTest
import org.junit.Test

class SavlukovButtonTest : BaseComposeTest() {

    @Test
    fun savlukovButton_displaysContent() {
        val buttonText = "Click Me"
        setSavlukovContent {
            SavlukovButton(onClick = {}) {
                Text(text = buttonText)
            }
        }

        composeTestRule.onNodeWithText(buttonText).assertIsDisplayed()
    }

    @Test
    fun savlukovButton_isClickable_whenEnabled() {
        var clicked = false
        setSavlukovContent {
            SavlukovButton(
                onClick = { clicked = true },
                enabled = true
            ) {
                Text(text = "Button")
            }
        }

        composeTestRule.onNodeWithText("Button").performClick()
        assert(clicked)
    }

    @Test
    fun savlukovButton_isNotClickable_whenDisabled() {
        var clicked = false
        setSavlukovContent {
            SavlukovButton(
                onClick = { clicked = true },
                enabled = false
            ) {
                Text(text = "Button")
            }
        }

        composeTestRule.onNodeWithText("Button").performClick()
        assert(!clicked)
    }
    
    @Test
    fun savlukovButton_hasCorrectSemantics() {
        setSavlukovContent {
            SavlukovButton(onClick = {}) {
                Text(text = "Accessible Button")
            }
        }

        composeTestRule.onNodeWithText("Accessible Button")
            .assert(hasClickAction())
            .assert(hasAnyChild(hasText("Accessible Button")))
    }
}
