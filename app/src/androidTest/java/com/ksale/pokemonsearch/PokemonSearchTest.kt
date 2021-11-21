package com.ksale.pokemonsearch

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.ksale.pokemonsearch.ui.PokemonSearchScreen
import com.ksale.pokemonsearch.viewmodels.PokemonMetaData
import com.ksale.pokemonsearch.viewmodels.UIState
import org.junit.Rule
import org.junit.Test

class PokemonSearchTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun should_show_search_bar_initially() {
        composeTestRule.setContent {
            PokemonSearchScreen(
                "",
                UIState.Loading(false),
                {},
                {}
            )
        }
        composeTestRule.onNode(hasText("Search Pokemon \"Charizard\""))
            .assertIsDisplayed()
    }

    @Test
    fun should_show_loader_if_loading_ui_state_is_emitted() {
        composeTestRule.setContent {
            PokemonSearchScreen(
                "",
                UIState.Loading(true),
                {},
                {}
            )
        }
        composeTestRule.onNodeWithContentDescription("API call in progress, Please wait")
            .assertIsDisplayed()
    }

    @Test
    fun should_show_PokemonDetailScreen() {
        composeTestRule.setContent {
            PokemonSearchScreen(
                "",
                UIState.PokemonDetailState(PokemonMetaData("Charizard", "", "Dummy text")),
                {},
                {}
            )
        }
        composeTestRule.onNodeWithText("Charizard")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Dummy text")
            .assertIsDisplayed()
    }

    @Test
    fun should_show_ErrorScreen_without_retry_on_emitting_error() {
        composeTestRule.setContent {
            PokemonSearchScreen(
                "",
                UIState.ErrorState(false, "Please try after some time"),
                {},
                {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Press icon to retry")
            .assertDoesNotExist()

        composeTestRule.onNodeWithText("Please try after some time")
            .assertIsDisplayed()
    }

    @Test
    fun should_show_ErrorScreen_with_retry_icon_on_emitting_error() {
        composeTestRule.setContent {
            PokemonSearchScreen(
                "",
                UIState.ErrorState(true, "Please try after some time"),
                {},
                {}
            )
        }
        composeTestRule.onNodeWithContentDescription("Press icon to retry")
            .assertIsDisplayed()

        composeTestRule.onNodeWithText("Please try after some time")
            .assertIsDisplayed()
    }
}