package com.vivacious.ecommerce.presentation.pokemondetail

sealed class ProductDetailEvent {
    data class LoadProduct(val productId: String) : ProductDetailEvent()
    data object ToggleFavorite : ProductDetailEvent()
} 