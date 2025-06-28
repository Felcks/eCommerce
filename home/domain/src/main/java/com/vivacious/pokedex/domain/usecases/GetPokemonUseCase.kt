package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetProductUseCase {
    suspend operator fun invoke(productId: String): Flow<Resource<Product?>>
}