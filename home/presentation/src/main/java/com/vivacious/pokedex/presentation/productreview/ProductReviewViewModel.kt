package com.vivacious.pokedex.presentation.productreview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivacious.pokedex.domain.models.ProductReview
import com.vivacious.pokedex.domain.models.Rating
import com.vivacious.pokedex.domain.usecases.ValidateProductReviewUseCase
import com.vivacious.pokedex.domain.usecases.ValidationError
import com.vivacious.pokedex.domain.usecases.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductReviewViewModel @Inject constructor(
    private val validateProductReviewUseCase: ValidateProductReviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductReviewState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: ProductReviewEvent) {
        when (event) {
            is ProductReviewEvent.UpdateUserName -> {
                _state.value = _state.value.copy(userName = event.userName)
            }
            is ProductReviewEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is ProductReviewEvent.UpdatePhoneNumber -> {
                _state.value = _state.value.copy(phoneNumber = event.phoneNumber)
            }
            is ProductReviewEvent.UpdatePromotionalCode -> {
                _state.value = _state.value.copy(promotionalCode = event.promotionalCode)
            }
            is ProductReviewEvent.UpdateDeliveryDate -> {
                _state.value = _state.value.copy(deliveryDate = event.deliveryDate)
            }
            is ProductReviewEvent.UpdateRating -> {
                _state.value = _state.value.copy(rating = event.rating)
            }
            ProductReviewEvent.SubmitReview -> {
                validateAndSubmitReview()
            }
        }
    }

    private fun validateAndSubmitReview() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val review = ProductReview(
                userName = _state.value.userName,
                email = _state.value.email,
                phoneNumber = _state.value.phoneNumber,
                promotionalCode = _state.value.promotionalCode,
                deliveryDate = _state.value.deliveryDate,
                rating = _state.value.rating
            )

            val validationResult = validateProductReviewUseCase(review)

            _state.value = _state.value.copy(
                isLoading = false,
                validationErrors = validationResult.errors,
                isSubmitted = validationResult.isValid
            )
        }
    }
}

data class ProductReviewState(
    val userName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val promotionalCode: String = "",
    val deliveryDate: String = "",
    val rating: Rating? = null,
    val isLoading: Boolean = false,
    val validationErrors: List<ValidationError> = emptyList(),
    val isSubmitted: Boolean = false
)

sealed class ProductReviewEvent {
    data class UpdateUserName(val userName: String) : ProductReviewEvent()
    data class UpdateEmail(val email: String) : ProductReviewEvent()
    data class UpdatePhoneNumber(val phoneNumber: String) : ProductReviewEvent()
    data class UpdatePromotionalCode(val promotionalCode: String) : ProductReviewEvent()
    data class UpdateDeliveryDate(val deliveryDate: String) : ProductReviewEvent()
    data class UpdateRating(val rating: Rating) : ProductReviewEvent()
    object SubmitReview : ProductReviewEvent()
} 