package com.vivacious.ecommerce.domain.data_sources

import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    fun saveProduct(product: Product): Flow<Boolean>
    fun getFavoriteProducts(): Flow<List<Product>>
    suspend fun isProductFavorite(productId: Int): Boolean
    suspend fun removeProductFromFavorites(productId: Int): Boolean
}