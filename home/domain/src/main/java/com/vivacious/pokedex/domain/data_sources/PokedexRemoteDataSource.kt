package com.vivacious.pokedex.domain.data_sources

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRemoteDataSource {
    suspend fun getProducts(pageSize: Int): Flow<PagingData<ProductSummary>>
    suspend fun searchProducts(query: String): Flow<PagingData<ProductSummary>>
    suspend fun getProduct(productId: String): Flow<Resource<Product?>>
}