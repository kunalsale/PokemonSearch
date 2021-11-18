package com.ksale.pokemon_sdk.data

import com.ksale.pokemon_sdk.api.models.PokemonResponse
import com.ksale.pokemon_sdk.api.models.PokemonSpeciesResponse
import com.ksale.pokemon_sdk.api.models.ShakespeareTranslationResponse

interface PokemonRepository {
    suspend fun fetchPokemon(pokemonName: String): PokemonResponse
    suspend fun fetchPokemonSpecies(pokemonName: String): PokemonSpeciesResponse
    suspend fun translateToShakespeare(textToTranslate: String): ShakespeareTranslationResponse
}