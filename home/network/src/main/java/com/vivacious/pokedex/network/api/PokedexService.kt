package com.vivacious.pokedex.network.api

import com.vivacious.pokedex.network.models.ProductResponse
import com.vivacious.pokedex.network.models.ProductsPageResponse
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

    @GET("products/{productId}")
    suspend fun getProduct(
        @Path(value = "productId") productId: String,
    ): Response<ProductResponse>
}