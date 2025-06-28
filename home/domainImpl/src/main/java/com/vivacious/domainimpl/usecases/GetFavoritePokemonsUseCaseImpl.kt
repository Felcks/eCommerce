package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.GetFavoriteProductsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : GetFavoriteProductsUseCase {

    override suspend fun invoke(): Flow<List<Product>> {
        return repository.getFavoriteProducts()
    }
}