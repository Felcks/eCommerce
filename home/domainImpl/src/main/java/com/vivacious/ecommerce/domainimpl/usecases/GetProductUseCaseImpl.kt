package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.GetProductUseCase
import com.vivacious.ecommerce.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductUseCaseImpl @Inject constructor(
    private val repositoryImpl: ProductRepository
) : GetProductUseCase {

    override suspend fun invoke(productId: String): Flow<Resource<Product?>> {
        return repositoryImpl.getProduct(productId)
    }

}