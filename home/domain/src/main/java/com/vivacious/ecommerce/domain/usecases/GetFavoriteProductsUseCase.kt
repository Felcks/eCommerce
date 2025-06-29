package com.vivacious.ecommerce.domain.usecases

import com.vivacious.ecommerce.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface GetFavoriteProductsUseCase {
    suspend operator fun invoke(): Flow<List<Product>>
}