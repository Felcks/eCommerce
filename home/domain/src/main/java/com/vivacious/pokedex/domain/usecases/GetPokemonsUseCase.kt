package com.vivacious.pokedex.domain.usecases

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface GetProductsUseCase {
    suspend operator fun invoke(): Flow<PagingData<ProductSummary>>
}