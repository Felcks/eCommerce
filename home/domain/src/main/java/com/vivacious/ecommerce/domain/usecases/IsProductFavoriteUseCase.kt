package com.vivacious.ecommerce.domain.usecases

interface IsProductFavoriteUseCase {
    suspend operator fun invoke(productId: Int): Boolean
} 