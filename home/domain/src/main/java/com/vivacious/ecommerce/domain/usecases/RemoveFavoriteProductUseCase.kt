package com.vivacious.ecommerce.domain.usecases

import kotlinx.coroutines.flow.Flow

interface RemoveFavoriteProductUseCase {
    suspend operator fun invoke(productId: Int): Flow<Boolean>
} 