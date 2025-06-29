package com.vivacious.ecommerce.domain.models

data class StoreReview(
    val userName: String,
    val email: String,
    val phoneNumber: String,
    val promotionalCode: String,
    val deliveryDate: String,
    val rating: Rating?
)

enum class Rating {
    BAD,
    FINE,
    GOOD,
    GREAT,
    EXCELLENT
} 