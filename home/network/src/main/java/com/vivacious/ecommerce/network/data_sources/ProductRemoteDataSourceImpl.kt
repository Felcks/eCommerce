package com.vivacious.ecommerce.network.data_sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vivacious.ecommerce.domain.data_sources.ProductRemoteDataSource
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.wrapper.Resource
import com.vivacious.ecommerce.network.api.ProductService
import com.vivacious.ecommerce.network.mappers.toProduct
import com.vivacious.ecommerce.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(private val productService: ProductService) : ProductRemoteDataSource {

    private var cachedPager: Pager<Int, ProductSummary>? = null
    private var cachedSearchPager: Pager<Int, ProductSummary>? = null
    private var lastSearchQuery: String? = null

    override suspend fun getProducts(pageSize: Int): Flow<PagingData<ProductSummary>> {
        clearSearchQueryAndCache()

        if (cachedPager == null) {
            val pagingConfig = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false,
                prefetchDistance = 5
            )
            cachedPager = Pager(
                config = pagingConfig,
                initialKey = 0,
                pagingSourceFactory = { ProductPagingSource(productService = productService, pageSize = pageSize) }
            )
        }
        
        return cachedPager!!.flow
    }

    private fun clearSearchQueryAndCache() {
        cachedSearchPager = null
        lastSearchQuery = null
    }

    override suspend fun searchProducts(query: String): Flow<PagingData<ProductSummary>> {
        if (lastSearchQuery != query || cachedSearchPager == null) {
            val pagingConfig = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5
            )
            cachedSearchPager = Pager(
                config = pagingConfig,
                initialKey = 0,
                pagingSourceFactory = { ProductSearchPagingSource(productService = productService, query = query) }
            )
            lastSearchQuery = query
        }
        
        return cachedSearchPager!!.flow
    }

    override suspend fun getProduct(productId: String): Flow<Resource<Product?>> {
        return safeApiCall(Dispatchers.IO) {
            productService.getProduct(productId).body()?.toProduct()
        }
    }
} 