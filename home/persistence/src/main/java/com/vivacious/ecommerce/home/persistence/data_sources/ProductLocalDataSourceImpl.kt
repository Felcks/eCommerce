package com.vivacious.ecommerce.home.persistence.data_sources

import com.vivacious.ecommerce.domain.data_sources.ProductLocalDataSource
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.home.persistence.database.AppDatabase
import com.vivacious.ecommerce.home.persistence.mappers.ProductMapper
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