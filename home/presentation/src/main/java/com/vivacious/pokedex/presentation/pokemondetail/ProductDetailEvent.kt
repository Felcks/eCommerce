package com.vivacious.pokedex.presentation.pokemondetail

sealed class ProductDetailEvent {
    data class LoadProduct(val productId: String) : ProductDetailEvent()
    data object AddProductAsFavorite : ProductDetailEvent()
} 