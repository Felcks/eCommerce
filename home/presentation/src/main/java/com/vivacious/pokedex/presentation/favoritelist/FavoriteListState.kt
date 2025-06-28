package com.vivacious.pokedex.presentation.favoritelist

import com.vivacious.pokedex.domain.models.Product

data class FavoriteListState(
    var loading: Boolean = false,
    var errorMessage: String? = null,
    var products: List<Product>? = null
)