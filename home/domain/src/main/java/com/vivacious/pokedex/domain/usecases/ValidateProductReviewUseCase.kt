package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.ProductReview
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

interface ValidateProductReviewUseCase {
    suspend operator fun invoke(review: ProductReview): ValidationResult
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<ValidationError> = emptyList()
)

sealed class ValidationError {
    object EmptyUserName : ValidationError()
    object EmptyEmail : ValidationError()
    object InvalidEmail : ValidationError()
    object EmptyPhoneNumber : ValidationError()
    object InvalidPhoneNumber : ValidationError()
    object EmptyPromotionalCode : ValidationError()
    object InvalidPromotionalCode : ValidationError()
    object EmptyDeliveryDate : ValidationError()
    object InvalidDeliveryDate : ValidationError()
    object MondayNotAllowed : ValidationError()
    object FutureDateNotAllowed : ValidationError()
    object EmptyRating : ValidationError()
} 