package com.vivacious.pokedex.presentation.home

sealed class HomeScreenEvent {
    data object GetFreshProducts : HomeScreenEvent()
    data object LoadMoreProducts : HomeScreenEvent()
    data class SearchProducts(val query: String) : HomeScreenEvent()
}