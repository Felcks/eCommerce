package com.vivacious.pokedex.network.models

import com.google.gson.annotations.SerializedName

data class ProductsPageResponse(
    @SerializedName("products")
    val products: List<ProductResponse>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("skip")
    val skip: Int,
    @SerializedName("limit")
    val limit: Int
) 