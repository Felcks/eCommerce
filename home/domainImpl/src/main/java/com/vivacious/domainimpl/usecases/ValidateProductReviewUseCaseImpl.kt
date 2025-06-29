package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.ProductReview
import com.vivacious.pokedex.domain.usecases.ValidateProductReviewUseCase
import com.vivacious.pokedex.domain.usecases.ValidationError
import com.vivacious.pokedex.domain.usecases.ValidationResult
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

class ValidateProductReviewUseCaseImpl @Inject constructor() : ValidateProductReviewUseCase {

    override suspend fun invoke(review: ProductReview): ValidationResult {
        val errors = mutableListOf<ValidationError>()

        // Validação do nome do usuário
        if (review.userName.isBlank()) {
            errors.add(ValidationError.EmptyUserName)
        }

        // Validação do email
        if (review.email.isBlank()) {
            errors.add(ValidationError.EmptyEmail)
        } else if (!isValidEmail(review.email)) {
            errors.add(ValidationError.InvalidEmail)
        }

        // Validação do número de telefone
        if (review.phoneNumber.isBlank()) {
            errors.add(ValidationError.EmptyPhoneNumber)
        } else if (!isValidPhoneNumber(review.phoneNumber)) {
            errors.add(ValidationError.InvalidPhoneNumber)
        }

        // Validação do código promocional
        if (review.promotionalCode.isBlank()) {
            errors.add(ValidationError.EmptyPromotionalCode)
        } else if (!isValidPromotionalCode(review.promotionalCode)) {
            errors.add(ValidationError.InvalidPromotionalCode)
        }

        // Validação da data de entrega
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
        // Apenas dígitos
        return phoneNumber.all { it.isDigit() }
    }

    private fun isValidPromotionalCode(code: String): Boolean {
        // Apenas letras maiúsculas e hífens, mínimo 3 e máximo 7 caracteres, sem acentos
        val codeRegex = "^[A-Z-]{3,7}$"
        return code.matches(codeRegex.toRegex())
    }

    private fun validateDeliveryDate(dateString: String): List<ValidationError> {
        val errors = mutableListOf<ValidationError>()
        
        try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val deliveryDate = LocalDate.parse(dateString, formatter)
            val today = LocalDate.now()

            // Verificar se não é segunda-feira
            if (deliveryDate.dayOfWeek == DayOfWeek.MONDAY) {
                errors.add(ValidationError.MondayNotAllowed)
            }

            // Verificar se não está no futuro
            if (deliveryDate.isAfter(today)) {
                errors.add(ValidationError.FutureDateNotAllowed)
            }

        } catch (e: DateTimeParseException) {
            errors.add(ValidationError.InvalidDeliveryDate)
        }

        return errors
    }
} 