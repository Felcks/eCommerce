package com.vivacious.ecommerce.network.data_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.network.api.ProductService
import com.vivacious.ecommerce.network.mappers.toProductSummary

class ProductPagingSource(val productService: ProductService, val pageSize: Int) : PagingSource<Int, ProductSummary>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductSummary> {
        return try {
            val page = params.key ?: 0
            val response = productService.getProducts(
                limit = pageSize,
                skip = page * pageSize,
            ).body()?.products.orEmpty()

            val productSummaries = response.map { it.toProductSummary() }

            LoadResult.Page(
                data = productSummaries,
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, ProductSummary>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
} 