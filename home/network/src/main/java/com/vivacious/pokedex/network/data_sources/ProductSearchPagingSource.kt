package com.vivacious.pokedex.network.data_sources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.network.api.ProductService
import com.vivacious.pokedex.network.mappers.toProductSummary

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
                prevKey = null, // Busca não suporta paginação
                nextKey = null  // Busca retorna todos os resultados de uma vez
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    
    override fun getRefreshKey(state: PagingState<Int, ProductSummary>): Int? {
        return null // Busca não suporta paginação
    }
} 