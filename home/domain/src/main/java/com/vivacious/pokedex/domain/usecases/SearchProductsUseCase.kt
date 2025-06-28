package com.vivacious.pokedex.domain.usecases

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface SearchProductsUseCase {
    suspend operator fun invoke(query: String): Flow<PagingData<ProductSummary>>
} 