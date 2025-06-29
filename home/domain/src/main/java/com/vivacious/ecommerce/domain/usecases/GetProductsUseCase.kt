package com.vivacious.ecommerce.domain.usecases

import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface GetProductsUseCase {
    suspend operator fun invoke(): Flow<PagingData<ProductSummary>>
}