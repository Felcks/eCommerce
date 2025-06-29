package com.vivacious.ecommerce.domain.data_sources

import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {
    suspend fun getProducts(pageSize: Int): Flow<PagingData<ProductSummary>>
    suspend fun searchProducts(query: String): Flow<PagingData<ProductSummary>>
    suspend fun getProduct(productId: String): Flow<Resource<Product?>>
}