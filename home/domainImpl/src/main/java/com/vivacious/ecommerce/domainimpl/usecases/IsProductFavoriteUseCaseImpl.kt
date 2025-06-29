package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.IsProductFavoriteUseCase
import javax.inject.Inject

class IsProductFavoriteUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : IsProductFavoriteUseCase {

    override suspend fun invoke(productId: Int): Boolean {
        return repository.isProductFavorite(productId)
    }
} 