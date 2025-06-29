package com.vivacious.ecommerce.presentation.storereview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivacious.ecommerce.domain.models.StoreReview
import com.vivacious.ecommerce.domain.models.Rating
import com.vivacious.ecommerce.domain.usecases.ValidateStoreReviewUseCase
import com.vivacious.ecommerce.domain.usecases.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreReviewViewModel @Inject constructor(
    private val validateStoreReviewUseCase: ValidateStoreReviewUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StoreReviewState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: StoreReviewEvent) {
        when (event) {
            is StoreReviewEvent.UpdateUserName -> {
                _state.value = _state.value.copy(userName = event.userName)
            }
            is StoreReviewEvent.UpdateEmail -> {
                _state.value = _state.value.copy(email = event.email)
            }
            is StoreReviewEvent.UpdatePhoneNumber -> {
                _state.value = _state.value.copy(phoneNumber = event.phoneNumber)
            }
            is StoreReviewEvent.UpdatePromotionalCode -> {
                _state.value = _state.value.copy(promotionalCode = event.promotionalCode)
            }
            is StoreReviewEvent.UpdateDeliveryDate -> {
                _state.value = _state.value.copy(deliveryDate = event.deliveryDate)
            }
            is StoreReviewEvent.UpdateRating -> {
                _state.value = _state.value.copy(rating = event.rating)
            }
            StoreReviewEvent.SubmitReview -> {
                validateAndSubmitReview()
            }
        }
    }

    private fun validateAndSubmitReview() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            val review = StoreReview(
                userName = _state.value.userName,
                email = _state.value.email,
                phoneNumber = _state.value.phoneNumber,
                promotionalCode = _state.value.promotionalCode,
                deliveryDate = _state.value.deliveryDate,
                rating = _state.value.rating
            )

            val validationResult = validateStoreReviewUseCase(review)

            _state.value = _state.value.copy(
                isLoading = false,
                validationErrors = validationResult.errors,
                isSubmitted = validationResult.isValid
            )
        }
    }
}

data class StoreReviewState(
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

sealed class StoreReviewEvent {
    data class UpdateUserName(val userName: String) : StoreReviewEvent()
    data class UpdateEmail(val email: String) : StoreReviewEvent()
    data class UpdatePhoneNumber(val phoneNumber: String) : StoreReviewEvent()
    data class UpdatePromotionalCode(val promotionalCode: String) : StoreReviewEvent()
    data class UpdateDeliveryDate(val deliveryDate: String) : StoreReviewEvent()
    data class UpdateRating(val rating: Rating) : StoreReviewEvent()
    data object SubmitReview : StoreReviewEvent()
} 