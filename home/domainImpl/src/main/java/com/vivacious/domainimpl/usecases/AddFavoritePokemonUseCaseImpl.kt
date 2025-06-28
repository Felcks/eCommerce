package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.AddFavoriteProductUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFavoriteProductUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : AddFavoriteProductUseCase {

    override suspend fun invoke(product: Product): Flow<Boolean> {
        return repository.saveProductAsFavorite(product)
    }
}