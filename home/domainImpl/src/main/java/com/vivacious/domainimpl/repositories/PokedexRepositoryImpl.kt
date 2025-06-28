package com.vivacious.domainimpl.repositories

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.data_sources.ProductLocalDataSource
import com.vivacious.pokedex.domain.data_sources.ProductRemoteDataSource
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.wrapper.Resource
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

    companion object {
        const val PAGE_SIZE = 20
    }
}