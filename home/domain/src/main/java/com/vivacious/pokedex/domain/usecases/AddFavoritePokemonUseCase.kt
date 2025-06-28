package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface AddFavoriteProductUseCase {
    suspend operator fun invoke(product: Product): Flow<Boolean>
}