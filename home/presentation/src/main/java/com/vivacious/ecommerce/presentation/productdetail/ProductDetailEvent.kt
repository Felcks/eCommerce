package com.vivacious.ecommerce.presentation.productdetail

sealed class ProductDetailEvent {
    data class LoadProduct(val productId: String) : ProductDetailEvent()
    data object ToggleFavorite : ProductDetailEvent()
} 