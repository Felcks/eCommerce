package com.vivacious.pokedex.domain.models

interface Product : ProductSummary {
    override val id: Int
    override val title: String
    val description: String
    val price: Double
    val discountPercentage: Double
    override val rating: Double
    val stock: Int
    val brand: String
    val category: String
    override val thumbnail: String
    override val images: List<String>
}

interface ProductSummary {
    val id: Int
    val title: String
    val thumbnail: String
    val images: List<String>
    val rating: Double
} 