package com.vivacious.ecommerce.domainimpl.usecases

import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.GetProductsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : GetProductsUseCase {

    override suspend fun invoke(): Flow<PagingData<ProductSummary>> {
        return productRepository.getProducts()
    }
}