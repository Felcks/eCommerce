package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.AddFavoriteProductUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddFavoriteProductUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : AddFavoriteProductUseCase {

    override suspend fun invoke(product: Product): Flow<Boolean> {
        return repository.saveProductAsFavorite(product)
    }
}