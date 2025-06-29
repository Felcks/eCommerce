package com.vivacious.ecommerce.presentation.favoritelist

import com.vivacious.ecommerce.domain.models.Product

data class FavoriteListState(
    var loading: Boolean = false,
    var errorMessage: String? = null,
    var products: List<Product>? = null
)