package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.RemoveFavoriteProductUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RemoveFavoriteProductUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : RemoveFavoriteProductUseCase {

    override suspend fun invoke(productId: Int): Flow<Boolean> {
        return repository.removeProductFromFavorites(productId)
    }
}