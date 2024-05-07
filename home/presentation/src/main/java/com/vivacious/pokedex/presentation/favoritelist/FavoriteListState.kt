package com.vivacious.pokedex.presentation.favoritelist

import com.vivacious.pokedex.domain.models.Pokemon

data class FavoriteListState(
    var loading: Boolean = false,
    var errorMessage: String? = null,
    var pokemons: List<Pokemon>? = null
)