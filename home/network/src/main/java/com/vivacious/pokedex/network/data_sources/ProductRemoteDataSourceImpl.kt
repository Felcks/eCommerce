package com.vivacious.pokedex.network.data_sources

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vivacious.pokedex.domain.data_sources.ProductRemoteDataSource
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.wrapper.Resource
import com.vivacious.pokedex.network.api.ProductService
import com.vivacious.pokedex.network.mappers.toProduct
import com.vivacious.pokedex.network.safeApiCall
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductRemoteDataSourceImpl @Inject constructor(private val productService: ProductService) : ProductRemoteDataSource {

    // Cache do Pager para preservar o estado
    private var cachedPager: Pager<Int, ProductSummary>? = null

    override suspend fun getProducts(pageSize: Int): Flow<PagingData<ProductSummary>> {
        // Reutilizar o Pager se já existir para preservar o estado
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

    override suspend fun getProduct(productId: String): Flow<Resource<Product?>> {
        return safeApiCall(Dispatchers.IO) {
            productService.getProduct(productId).body()?.toProduct()
        }
    }
} 