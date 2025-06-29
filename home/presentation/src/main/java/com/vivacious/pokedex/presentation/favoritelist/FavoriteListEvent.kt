package com.vivacious.pokedex.presentation.favoritelist

sealed class FavoriteListEvent {
    data object LoadFavoriteProducts : FavoriteListEvent()
    data class RemoveFavoriteProduct(val productId: Int) : FavoriteListEvent()
}