package com.ksale.pokemonsearch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksale.pokemon_sdk.api.models.FlavorTextEntry
import com.ksale.pokemon_sdk.usecase.PokemonState
import com.ksale.pokemon_sdk.usecase.PokemonUseCase
import com.ksale.pokemon_sdk.usecase.ShakespeareTranslatorUseCase
import com.ksale.pokemon_sdk.usecase.TranslatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var pokemonUseCase: PokemonUseCase

    @Inject
    lateinit var shakespeareTranslatorUseCase: ShakespeareTranslatorUseCase

    private var pokemonDetail = PokemonMetaData()

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading(false))
    val uiState: StateFlow<UIState> = _uiState

    // Fetches the sprite url for the pokemon name
    fun getPokemon(pokemonName: String) {
        viewModelScope.launch {
            _uiState.emit(UIState.Loading(true))
            when (val result = pokemonUseCase.getPokemon(pokemonName = pokemonName)) {
                is PokemonState.Pokemon -> {
                    val pokemon = result.response
                    pokemonDetail.pokemonName = pokemonName
                    pokemonDetail.pokemonSprite = pokemon.sprites.front_default
                    _uiState.emit(UIState.PokemonDetailState(pokemonDetail.copy()))
                }
                is PokemonState.PokemonError -> {
                    _uiState.emit(UIState.ErrorState(result.shouldRetry, result.errorMessage))
                }
                else -> {
                    _uiState.emit(UIState.ErrorState(true, "Something went wrong"))
                }
            }
        }
    }

    // Fetches the sprite url for the pokemon name
    fun getShakeSpeareanDescription(pokemonName: String) {
        viewModelScope.launch {
            when (val result = pokemonUseCase.getPokemonSpecies(pokemonName = pokemonName)) {
                is PokemonState.PokemonSpecies -> {
                    val pokemonSpecies = result.response
                    val pokemonFlavorText = getEnglishLanguageFlavorEntry(pokemonSpecies.flavor_text_entries)
                    translateTheDescription(pokemonFlavorText)
                }
                is PokemonState.PokemonError -> {
                    _uiState.emit(UIState.ErrorState(result.shouldRetry, result.errorMessage))
                }
                else -> {
                    _uiState.emit(UIState.ErrorState(true, "Something went wrong"))
                }
            }
        }
    }

    private suspend fun getEnglishLanguageFlavorEntry(entries: List<FlavorTextEntry>): String =
        withContext(Dispatchers.Default) {
            var flavorText = ""
            for (flavor_entry in entries) {
                if (flavor_entry.language.name == "en") {
                    flavorText = flavor_entry.flavor_text
                    break
                }
            }
            flavorText
        }

    private suspend fun translateTheDescription(flavorText: String) {
        val result = shakespeareTranslatorUseCase.translateToShakespeare(flavorText)
        _uiState.emit(UIState.Loading(false))
        when (result) {
            is TranslatorState.ShakespeareTranslated -> {
                pokemonDetail.translatedString = result.translated
                _uiState.emit(UIState.PokemonDetailState(pokemonDetail.copy()))
            }
            is TranslatorState.ShakespeareError -> {
                _uiState.emit(UIState.PokemonDetailState(pokemonDetail.copy()))
            }
        }
    }
}

sealed class UIState {
    data class Loading(val isLoading: Boolean) : UIState()
    data class PokemonDetailState(val pokemonMetaData: PokemonMetaData) : UIState()
    data class ErrorState(val shouldRetry: Boolean, val errorMessage: String) : UIState()
}

data class PokemonMetaData(
    var pokemonName: String = "",
    var pokemonSprite: String = "",
    var translatedString: String = ""
)