package com.ksale.pokemon_sdk.api.models

data class ShakespeareTranslationResponse(
    val contents: Contents,
    val success: Success
)

data class Contents(
    val text: String,
    val translated: String,
    val translation: String
)

data class Success(
    val total: Int
)