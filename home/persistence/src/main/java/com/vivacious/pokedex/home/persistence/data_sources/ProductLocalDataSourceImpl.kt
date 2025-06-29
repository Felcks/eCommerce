package com.vivacious.pokedex.home.persistence.data_sources

import com.vivacious.pokedex.domain.data_sources.ProductLocalDataSource
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.home.persistence.database.AppDatabase
import com.vivacious.pokedex.home.persistence.mappers.ProductMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductLocalDataSourceImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val productMapper: ProductMapper
) : ProductLocalDataSource {

    override fun saveProduct(product: Product): Flow<Boolean> {
        return kotlinx.coroutines.flow.flow {
            try {
                val entity = productMapper.toEntity(product)
                appDatabase.productDao().insertProduct(entity)
                emit(true)
            } catch (e: Exception) {
                emit(false)
            }
        }
    }

    override fun getFavoriteProducts(): Flow<List<Product>> {
        return appDatabase.productDao().getFavoriteProducts()
            .map { entities ->
                entities.map { entity ->
                    productMapper.toDomain(entity)
                }
            }
    }

    override fun searchProducts(query: String): Flow<List<ProductSummary>> {
        // Implementar busca local se necessário
        return kotlinx.coroutines.flow.flowOf(emptyList())
    }
    
    override suspend fun isProductFavorite(productId: Int): Boolean {
        return appDatabase.productDao().isProductFavorite(productId)
    }
    
    override suspend fun removeProductFromFavorites(productId: Int): Boolean {
        return try {
            appDatabase.productDao().deleteProductById(productId)
            true
        } catch (e: Exception) {
            false
        }
    }
} 