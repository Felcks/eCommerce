package com.vivacious.pokedex.domain.models

data class ProductReview(
    val userName: String,
    val email: String,
    val phoneNumber: String,
    val promotionalCode: String,
    val deliveryDate: String,
    val rating: Rating?
)

enum class Rating {
    MAU,
    SATISFATORIO,
    BOM,
    MUITO_BOM,
    EXCELENTE
} 