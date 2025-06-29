package com.vivacious.ecommerce.domainimpl.usecases

import com.vivacious.ecommerce.domain.models.StoreReview
import com.vivacious.ecommerce.domain.usecases.ValidateStoreReviewUseCase
import com.vivacious.ecommerce.domain.usecases.ValidationError
import com.vivacious.ecommerce.domain.usecases.ValidationResult
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class ValidateStoreReviewUseCaseImpl @Inject constructor() : ValidateStoreReviewUseCase {

    override suspend fun invoke(review: StoreReview): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        if (review.userName.isBlank()) {
            errors.add(ValidationError.EmptyUserName)
        }

        if (review.email.isBlank()) {
            errors.add(ValidationError.EmptyEmail)
        } else if (!isValidEmail(review.email)) {
            errors.add(ValidationError.InvalidEmail)
        }

        if (review.phoneNumber.isBlank()) {
            errors.add(ValidationError.EmptyPhoneNumber)
        } else if (!isValidPhoneNumber(review.phoneNumber)) {
            errors.add(ValidationError.InvalidPhoneNumber)
        }

        if (review.promotionalCode.isBlank()) {
            errors.add(ValidationError.EmptyPromotionalCode)
        } else if (!isValidPromotionalCode(review.promotionalCode)) {
            errors.add(ValidationError.InvalidPromotionalCode)
        }

        if (review.deliveryDate.isBlank()) {
            errors.add(ValidationError.EmptyDeliveryDate)
        } else {
            val dateValidation = validateDeliveryDate(review.deliveryDate)
            errors.addAll(dateValidation)
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        return email.matches(emailRegex.toRegex())
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.all { it.isDigit() }
    }

    private fun isValidPromotionalCode(code: String): Boolean {
        val codeRegex = "^[A-Z-]{3,7}$"
        return code.matches(codeRegex.toRegex())
    }

    private fun validateDeliveryDate(dateString: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
        
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val deliveryDate = LocalDate.parse(dateString, formatter)
            val today = LocalDate.now()

            if (deliveryDate.dayOfWeek == DayOfWeek.MONDAY) {
                errors.add(ValidationError.MondayNotAllowed)
            }

            if (deliveryDate.isAfter(today)) {
                errors.add(ValidationError.FutureDateNotAllowed)
            }

        } catch (e: DateTimeParseException) {
            errors.add(ValidationError.InvalidDeliveryDate)
        }

        return errors
    }
} 