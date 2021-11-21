package com.ksale.pokemonsearch

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ksale.pokemonsearch.ui.PokemonSearchScreen
import com.ksale.pokemonsearch.ui.SearchBar
import com.ksale.pokemonsearch.viewmodels.UIState
import org.junit.Rule
import org.junit.Test

class SearchBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    internal fun should_show_clear_icon_once_User_starts_typing() {
        composeTestRule.setContent {
            SearchBar(
                "abc",
                {},
                {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Press icon to clear the text")
            .assertIsDisplayed()
    }

    @Test
    fun should_clear_the_text_on_clear_click() {
        composeTestRule.setContent {
            val query = remember { mutableStateOf("abc") }
            SearchBar(
                query.value,
                {
                    query.value = it
                },
                {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Press icon to clear the text")
            .performClick()
        composeTestRule.onNode(hasText("Search Pokemon \"Charizard\""))
            .assertIsDisplayed()
    }
}