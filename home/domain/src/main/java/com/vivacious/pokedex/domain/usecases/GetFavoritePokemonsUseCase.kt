package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface GetFavoriteProductsUseCase {
    suspend operator fun invoke(): Flow<List<Product>>
}