package com.vivacious.pokedex.network.mappers

import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.network.models.ProductResponse

fun ProductResponse.toProduct(): Product {
    return object : Product {
        override val id: Int = this@toProduct.id
        override val title: String = this@toProduct.title
        override val description: String = this@toProduct.description
        override val price: Double = this@toProduct.price
        override val discountPercentage: Double = this@toProduct.discountPercentage
        override val rating: Double = this@toProduct.rating
        override val stock: Int = this@toProduct.stock
        override val brand: String = this@toProduct.brand
        override val category: String = this@toProduct.category
        override val thumbnail: String = this@toProduct.thumbnail
        override val images: List<String> = this@toProduct.images
    }
}

fun ProductResponse.toProductSummary(): ProductSummary {
    return object : ProductSummary {
        override val id: Int = this@toProductSummary.id
        override val title: String = this@toProductSummary.title
        override val thumbnail: String = this@toProductSummary.thumbnail
        override val images: List<String> = this@toProductSummary.images
        override val rating: Double = this@toProductSummary.rating
    }
} 