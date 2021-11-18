package com.ksale.pokemon_sdk.api

import com.ksale.pokemon_sdk.api.models.PokemonResponse
import com.ksale.pokemon_sdk.api.models.PokemonSpeciesResponse
import com.ksale.pokemon_sdk.api.models.ShakespeareTranslationResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface PokemonService {

    @GET
    suspend fun pokemon(@Url url: String): PokemonResponse

    @GET
    suspend fun pokemonSpecies(@Url url: String): PokemonSpeciesResponse

    @GET
    suspend fun translateDescription(@Url url: String): ShakespeareTranslationResponse

    companion object {
        fun create(): PokemonService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl("https://pokeapi.co/docs/v2/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(PokemonService::class.java)
        }
    }
}