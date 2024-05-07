package com.vivacious.pokedex.presentation.pokemondetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivacious.pokedex.domain.usecases.AddFavoritePokemonUseCase
import com.vivacious.pokedex.domain.usecases.GetPokemonUseCase
import com.vivacious.pokedex.domain.wrapper.onFailure
import com.vivacious.pokedex.domain.wrapper.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val getPokemonUseCase: GetPokemonUseCase,
    private val addFavoritePokemonUseCase: AddFavoritePokemonUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private var pokemonId: String? = null

    private val _state = MutableStateFlow(PokemonDetailState())
    val state = _state.asStateFlow()

    init {
        pokemonId = savedStateHandle["pokemonId"]
    }

    fun handleScreenEvents(pokemonDetailEvent: PokemonDetailEvent) {
        when(pokemonDetailEvent) {
            PokemonDetailEvent.LoadPokemon -> loadPokemon()
            PokemonDetailEvent.AddPokemonAsFavorite -> addPokemonAsFavorite()
        }
    }

    private fun loadPokemon() {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonId?.let {
                _state.value = _state.value.copy(loading = true, errorMessage = null)

                getPokemonUseCase.invoke(it).collectLatest { result ->
                    result.onSuccess {
                        _state.value = _state.value.copy(loading = false, errorMessage = null, pokemon = it)
                    }
                    result.onFailure {
                        _state.value = _state.value.copy(loading = false, errorMessage = it)
                    }
                }
            }
        }
    }

    private fun addPokemonAsFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val pokemon = _state.value.pokemon

            pokemon?.let {
                addFavoritePokemonUseCase.invoke(it).collectLatest {
                    Log.i("script2", "Pokemon inserted correctly $it")
                }
            }
        }
    }
}