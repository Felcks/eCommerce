package com.vivacious.pokedex.presentation.pokemondetail

sealed class ProductDetailEvent {
    data object LoadProduct : ProductDetailEvent()
    data object AddProductAsFavorite : ProductDetailEvent()
} 