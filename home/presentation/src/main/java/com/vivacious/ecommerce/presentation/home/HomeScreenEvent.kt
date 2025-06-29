package com.vivacious.ecommerce.presentation.home

sealed class HomeScreenEvent {
    data object GetFreshProducts : HomeScreenEvent()
    data object LoadMoreProducts : HomeScreenEvent()
    data class SearchProducts(val query: String) : HomeScreenEvent()
}