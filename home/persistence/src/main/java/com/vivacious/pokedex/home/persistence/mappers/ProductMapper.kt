package com.vivacious.pokedex.home.persistence.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductData
import com.vivacious.pokedex.home.persistence.models.ProductEntity

class ProductMapper {
    
    private val gson = Gson()
    
    fun toEntity(product: Product): ProductEntity {
        return ProductEntity(
            id = product.id,
            title = product.title,
            description = product.description,
            price = product.price,
            discountPercentage = product.discountPercentage,
            rating = product.rating,
            stock = product.stock,
            brand = product.brand,
            category = product.category,
            thumbnail = product.thumbnail,
            images = gson.toJson(product.images)
        )
    }
    
    fun toDomain(entity: ProductEntity): Product {
        val imagesType = object : TypeToken<List<String>>() {}.type
        val images = gson.fromJson<List<String>>(entity.images, imagesType)
        
        return ProductData(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            price = entity.price,
            discountPercentage = entity.discountPercentage,
            rating = entity.rating,
            stock = entity.stock,
            brand = entity.brand,
            category = entity.category,
            thumbnail = entity.thumbnail,
            images = images
        )
    }
} 