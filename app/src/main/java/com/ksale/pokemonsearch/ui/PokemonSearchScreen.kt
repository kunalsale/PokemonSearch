package com.ksale.pokemonsearch.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksale.pokemon_sdk.ui.PokemonDetailScreen
import com.ksale.pokemonsearch.R
import com.ksale.pokemonsearch.viewmodels.PokemonSearchViewModel
import com.ksale.pokemonsearch.viewmodels.UIState

@Composable
fun PokemonSearchScreen(viewModel: PokemonSearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    // To persist the query through screen rotation
    var query by rememberSaveable { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    PokemonSearchScreen(
        query = query,
        uiState = uiState,
        onValueChanged = {
            query = it
        },
        onSearch = {
            viewModel.getPokemon(it)
            viewModel.getShakeSpeareanDescription(it)
        }
    )
}

@Composable
fun PokemonSearchScreen(
    query: String,
    uiState: UIState,
    onValueChanged: (query: String) -> Unit,
    onSearch: (query: String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
    ) {
        SearchBar(
            query = query,
            onValueChange = onValueChanged,
            onSearch = onSearch
        )
        when (uiState) {
            is UIState.Loading -> {
                if (uiState.isLoading) {
                    LoadingScreen()
                }
            }
            is UIState.PokemonDetailState -> {
                val pokemonDetail = uiState.pokemonMetaData
                PokemonDetailScreen(
                    pokemonName = pokemonDetail.pokemonName,
                    pokemonSpriteUrl = pokemonDetail.pokemonSprite,
                    shakeSpeareTranslation = pokemonDetail.translatedString
                )
            }
            is UIState.ErrorState -> {
                ErrorScreen(
                    shouldRetry = uiState.shouldRetry,
                    errorMessage = uiState.errorMessage,
                    onRefreshClick = {
                        onSearch(query)
                    })
            }
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onValueChange: (str: String) -> Unit,
    onSearch: (str: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp
    ) {
        OutlinedTextField(
            placeholder = {
                Text(
                    text = "Search Pokemon \"Charizard\"",
                    fontStyle = FontStyle.Italic,
                    color = Color.Black
                )
            },
            value = query,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = Color.DarkGray,
            ),
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Red,
                unfocusedBorderColor = Color.Transparent,
                trailingIconColor = Color.DarkGray,
            ),
            trailingIcon = {
                Icon(
                    imageVector = if (query.isEmpty()) Icons.Filled.Search else Icons.Filled.Clear,
                    contentDescription = if (query.isEmpty()) "" else "Press icon to clear the text" ,
                    modifier = Modifier.clickable {
                        onValueChange("")
                    })
            },
            keyboardActions = KeyboardActions(onSearch = {
                onSearch(query)
            }),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Search
            ),
        )
    }
}

@Composable
fun LoadingScreen() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.wrapContentSize(),
            painter = painterResource(id = R.drawable.pokeball),
            contentDescription = "API call in progress, Please wait"
        )
    }
}

@Composable
fun ErrorScreen(shouldRetry: Boolean, errorMessage: String, onRefreshClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (shouldRetry) {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Press icon to retry",
                modifier = Modifier
                    .clickable {
                        onRefreshClick()
                    }
                    .size(50.dp)
            )
        }
        Text(
            text = errorMessage,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}