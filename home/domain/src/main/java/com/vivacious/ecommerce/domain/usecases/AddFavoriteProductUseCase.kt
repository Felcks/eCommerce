package com.vivacious.ecommerce.domain.usecases

import com.vivacious.ecommerce.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface AddFavoriteProductUseCase {
    suspend operator fun invoke(product: Product): Flow<Boolean>
}