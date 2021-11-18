package com.ksale.pokemon_sdk.data

import com.ksale.pokemon_sdk.api.PokemonService
import com.ksale.pokemon_sdk.api.models.PokemonResponse
import com.ksale.pokemon_sdk.api.models.PokemonSpeciesResponse
import com.ksale.pokemon_sdk.api.models.ShakespeareTranslationResponse

class PokemonRepositoryImpl(private val pokemonService: PokemonService): PokemonRepository {
    override suspend fun fetchPokemon(pokemonName: String): PokemonResponse {
        return pokemonService.pokemon("https://pokeapi.co/api/v2/pokemon/${pokemonName}/")
    }

    override suspend fun fetchPokemonSpecies(pokemonName: String): PokemonSpeciesResponse {
        return pokemonService.pokemonSpecies("https://pokeapi.co/api/v2/pokemon-species/${pokemonName}/")
    }

    override suspend fun translateToShakespeare(textToTranslate: String): ShakespeareTranslationResponse {
        return pokemonService.translateDescription("https://api.funtranslations.com/translate/shakespeare.json?text=${textToTranslate}")
    }
}