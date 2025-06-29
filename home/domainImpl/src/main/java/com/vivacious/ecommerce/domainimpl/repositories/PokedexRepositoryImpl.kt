package com.vivacious.ecommerce.domainimpl.repositories

import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.data_sources.ProductLocalDataSource
import com.vivacious.ecommerce.domain.data_sources.ProductRemoteDataSource
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource,
    private val productLocalDataSource: ProductLocalDataSource,
) : ProductRepository {

    override suspend fun getProducts(): Flow<PagingData<ProductSummary>> {
        return productRemoteDataSource.getProducts(PAGE_SIZE)
    }

    override suspend fun searchProducts(query: String): Flow<PagingData<ProductSummary>> {
        return productRemoteDataSource.searchProducts(query)
    }

    override suspend fun getProduct(productId: String): Flow<Resource<Product?>> {
        return productRemoteDataSource.getProduct(productId)
    }

    override suspend fun saveProductAsFavorite(product: Product): Flow<Boolean> {
        return productLocalDataSource.saveProduct(product)
    }

    override suspend fun getFavoriteProducts(): Flow<List<Product>> {
        return productLocalDataSource.getFavoriteProducts()
    }
    
    override suspend fun removeProductFromFavorites(productId: Int): Flow<Boolean> {
        return kotlinx.coroutines.flow.flow {
            val result = productLocalDataSource.removeProductFromFavorites(productId)
            emit(result)
        }
    }
    
    override suspend fun isProductFavorite(productId: Int): Boolean {
        return productLocalDataSource.isProductFavorite(productId)
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}