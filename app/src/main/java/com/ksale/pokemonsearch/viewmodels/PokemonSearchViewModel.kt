package com.ksale.pokemonsearch.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksale.pokemon_sdk.usecase.PokemonState
import com.ksale.pokemon_sdk.usecase.PokemonUseCase
import com.ksale.pokemon_sdk.usecase.ShakespeareTranslatorUseCase
import com.ksale.pokemon_sdk.usecase.TranslatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonSearchViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var pokemonUseCase: PokemonUseCase

    @Inject
    lateinit var shakespeareTranslatorUseCase: ShakespeareTranslatorUseCase

    private var pokemonDetail = PokemonMetaData()
    var query = mutableStateOf("")
    private val _uiState = MutableStateFlow<UIState>(UIState.Loading(false))
    val uiState: StateFlow<UIState> = _uiState

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

    fun getShakeSpeareanDescription(pokemonName: String) {
        viewModelScope.launch {
            when (val result = pokemonUseCase.getPokemonSpecies(pokemonName = pokemonName)) {
                is PokemonState.PokemonSpecies -> {
                    val pokemonSpecies = result.response
                    Log.i("ViewModel", " getShakeSpeareanDescription " + pokemonDetail.toString())
                    translateTheDescription(
                        pokemonSpecies.flavor_text_entries[0].flavor_text
                    )
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

    private suspend fun translateTheDescription(flavorText: String) {
        val result = shakespeareTranslatorUseCase.translateToShakespeare(flavorText)
        _uiState.emit(UIState.Loading(false))
        when (result) {
            is TranslatorState.ShakespeareTranslated -> {
                pokemonDetail.translatedString = result.translated
                Log.i("ViewModel", " translateTheDescription " + pokemonDetail.toString())
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