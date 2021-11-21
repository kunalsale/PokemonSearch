package com.ksale.pokemonsearch

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class PokemonUITest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun should_show_Toolbar_on_the_top() {
        composeTestRule.onNodeWithText("Pokemon Search")
            .assertIsDisplayed()
    }
}