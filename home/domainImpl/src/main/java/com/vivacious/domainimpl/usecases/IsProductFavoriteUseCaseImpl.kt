package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.IsProductFavoriteUseCase
import javax.inject.Inject

class IsProductFavoriteUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : IsProductFavoriteUseCase {

    override suspend fun invoke(productId: Int): Boolean {
        return repository.isProductFavorite(productId)
    }
} 