package com.vivacious.ecommerce.domain.usecases

import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface GetProductUseCase {
    suspend operator fun invoke(productId: String): Flow<Resource<Product?>>
}