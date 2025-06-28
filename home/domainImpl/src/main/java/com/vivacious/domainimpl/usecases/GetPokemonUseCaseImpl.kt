package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.GetProductUseCase
import com.vivacious.pokedex.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCaseImpl @Inject constructor(
    private val repositoryImpl: ProductRepository
) : GetProductUseCase {

    override suspend fun invoke(productId: String): Flow<Resource<Product?>> {
        return repositoryImpl.getProduct(productId)
    }

}