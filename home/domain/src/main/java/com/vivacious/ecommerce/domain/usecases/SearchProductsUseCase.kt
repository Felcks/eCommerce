package com.vivacious.ecommerce.domain.usecases

import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface SearchProductsUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<ProductSummary>>
} 