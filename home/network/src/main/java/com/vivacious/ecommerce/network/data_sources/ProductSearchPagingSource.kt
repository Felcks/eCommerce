package com.vivacious.ecommerce.network.data_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.network.api.ProductService
import com.vivacious.ecommerce.network.mappers.toProductSummary

class ProductSearchPagingSource(
    val productService: ProductService, 
    val query: String
) : PagingSource<Int, ProductSummary>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductSummary> {
        return try {
            val response = productService.searchProducts(query).body()
            val products = response?.products.orEmpty()

            LoadResult.Page(
                data = products.map { it.toProductSummary() },
                prevKey = null,
                nextKey = null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, ProductSummary>): Int? {
        return null
    }
} 