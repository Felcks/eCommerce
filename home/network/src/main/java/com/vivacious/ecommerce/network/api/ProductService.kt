package com.vivacious.ecommerce.network.api

import com.vivacious.ecommerce.network.models.ProductResponse
import com.vivacious.ecommerce.network.models.ProductsPageResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products")
    suspend fun getProducts(
        @Query(value = "limit") limit: Int,
        @Query(value = "skip") skip: Int
    ): Response<ProductsPageResponse>

    @GET("products/search")
    suspend fun searchProducts(
        @Query(value = "q") query: String
    ): Response<ProductsPageResponse>

    @GET("products/{productId}")
    suspend fun getProduct(
        @Path(value = "productId") productId: String,
    ): Response<ProductResponse>
}