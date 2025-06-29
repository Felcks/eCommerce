package com.vivacious.pokedex.domain.repositories

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(): Flow<PagingData<ProductSummary>>
    suspend fun searchProducts(query: String): Flow<PagingData<ProductSummary>>
    suspend fun getProduct(productId: String): Flow<Resource<Product?>>
    suspend fun saveProductAsFavorite(product: Product): Flow<Boolean>
    suspend fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun removeProductFromFavorites(productId: Int): Flow<Boolean>
    suspend fun isProductFavorite(productId: Int): Boolean
}