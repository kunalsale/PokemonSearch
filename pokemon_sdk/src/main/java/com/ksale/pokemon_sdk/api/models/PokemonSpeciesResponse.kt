package com.ksale.pokemon_sdk.api.models

data class PokemonSpeciesResponse(
    val flavor_text_entries: List<FlavorTextEntry>
)

data class FlavorTextEntry(
    val flavor_text: String,
    val language: Language,
    val version: Version
)

data class Language(
    val name: String,
    val url: String
)

data class Version(
    val name: String,
    val url: String
)

