package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.GetFavoriteProductsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteProductsUseCaseImpl @Inject constructor(
    private val repository: ProductRepository,
) : GetFavoriteProductsUseCase {

    override suspend fun invoke(): Flow<List<Product>> {
        return repository.getFavoriteProducts()
    }
}