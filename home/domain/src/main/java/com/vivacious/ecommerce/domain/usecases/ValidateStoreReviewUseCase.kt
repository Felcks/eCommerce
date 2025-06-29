package com.vivacious.ecommerce.domain.usecases

import com.vivacious.ecommerce.domain.models.StoreReview

interface ValidateStoreReviewUseCase {
    suspend operator fun invoke(review: StoreReview): ValidationResult
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<ValidationError> = emptyList()
)

sealed class ValidationError {
    data object EmptyUserName : ValidationError()
    data object EmptyEmail : ValidationError()
    data object InvalidEmail : ValidationError()
    data object EmptyPhoneNumber : ValidationError()
    data object InvalidPhoneNumber : ValidationError()
    data object EmptyPromotionalCode : ValidationError()
    data object InvalidPromotionalCode : ValidationError()
    data object EmptyDeliveryDate : ValidationError()
    data object InvalidDeliveryDate : ValidationError()
    data object MondayNotAllowed : ValidationError()
    data object FutureDateNotAllowed : ValidationError()
    data object EmptyRating : ValidationError()
} 