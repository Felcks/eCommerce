package com.vivacious.pokedex.home.persistence.data_sources

import com.vivacious.pokedex.domain.data_sources.ProductLocalDataSource
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.home.persistence.database.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase
) : ProductLocalDataSource {

    override fun saveProduct(product: Product): Flow<Boolean> {
        return flowOf(true) // TODO: Implement actual database save
    }

    override fun getFavoriteProducts(): Flow<List<Product>> {
        return flowOf(emptyList()) // TODO: Implement actual database query
    }

    override fun searchProducts(query: String): Flow<List<ProductSummary>> {
        return flowOf(emptyList()) // TODO: Implement actual database search
    }
} 