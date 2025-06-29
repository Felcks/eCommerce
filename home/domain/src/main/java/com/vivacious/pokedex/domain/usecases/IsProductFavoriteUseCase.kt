package com.vivacious.pokedex.domain.usecases

interface IsProductFavoriteUseCase {
    suspend operator fun invoke(productId: Int): Boolean
} 