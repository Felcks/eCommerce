package com.vivacious.domainimpl.usecases

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.GetProductsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : GetProductsUseCase {

    override suspend fun invoke(): Flow<PagingData<ProductSummary>> {
        return productRepository.getProducts()
    }
}