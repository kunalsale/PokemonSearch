package com.ksale.pokemonsearch.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ksale.pokemonsearch.ui.theme.PokemonSearchTheme

@Composable
fun PokemonSearchApp() {
    PokemonSearchTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    elevation = 8.dp
                ) {
                    Text(
                        text = "Pokemon Search",
                        modifier = Modifier.padding(start = 16.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
            }
        ) {
            PokemonSearchScreen()
        }
    }
}