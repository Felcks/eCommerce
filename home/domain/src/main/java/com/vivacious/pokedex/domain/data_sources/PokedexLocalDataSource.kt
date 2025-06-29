package com.vivacious.pokedex.domain.data_sources

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import kotlinx.coroutines.flow.Flow

interface ProductLocalDataSource {
    fun saveProduct(product: Product): Flow<Boolean>
    fun getFavoriteProducts(): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<ProductSummary>>
    suspend fun isProductFavorite(productId: Int): Boolean
    suspend fun removeProductFromFavorites(productId: Int): Boolean
}