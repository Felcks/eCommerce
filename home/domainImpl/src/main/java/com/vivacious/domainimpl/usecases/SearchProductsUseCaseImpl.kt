package com.vivacious.domainimpl.usecases

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.SearchProductsUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchProductsUseCaseImpl @Inject constructor(
    private val productRepository: ProductRepository
) : SearchProductsUseCase {

    override suspend fun invoke(query: String): Flow<PagingData<ProductSummary>> {
        return productRepository.searchProducts(query)
    }
} 