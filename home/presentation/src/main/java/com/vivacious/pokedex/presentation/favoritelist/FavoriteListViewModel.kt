package com.vivacious.pokedex.presentation.favoritelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.domain.models.PokemonSummary
import com.vivacious.pokedex.domain.usecases.GetFavoritePokemonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val getFavoritePokemonsUseCase: GetFavoritePokemonsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<FavoriteListState> = MutableStateFlow(FavoriteListState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: FavoriteListEvent) {
        when (event) {
            FavoriteListEvent.LoadFavoritePokemons -> loadFavoritePokemons()
        }
    }

    private fun loadFavoritePokemons() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(loading = true)
            getFavoritePokemonsUseCase.invoke()
                .catch {
                    _state.value = _state.value.copy(loading = false, errorMessage = it.message)
                }
                .collectLatest {
                    _state.value =
                        _state.value.copy(loading = false, errorMessage = null, pokemons = it)
                }
        }
    }
}