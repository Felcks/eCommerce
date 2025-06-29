package com.vivacious.ecommerce.presentation.storereview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.vivacious.ecommerce.core.presentation.theme.EcommerceTheme
import com.vivacious.ecommerce.domain.models.Rating
import com.vivacious.ecommerce.domain.usecases.ValidationError
import com.vivacious.ecommerce.domain.usecases.ValidateStoreReviewUseCase
import com.vivacious.ecommerce.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreReviewScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: StoreReviewViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.store_evaluation), fontSize = 24.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = state.userName,
                onValueChange = { viewModel.handleScreenEvents(StoreReviewEvent.UpdateUserName(it)) },
                label = { Text(stringResource(R.string.user_name_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyUserName)
            )
            if (state.validationErrors.contains(ValidationError.EmptyUserName)) {
                Text(
                    text = stringResource(R.string.user_name_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.handleScreenEvents(StoreReviewEvent.UpdateEmail(it)) },
                label = { Text(stringResource(R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyEmail) ||
                        state.validationErrors.contains(ValidationError.InvalidEmail)
            )
            if (state.validationErrors.contains(ValidationError.EmptyEmail)) {
                Text(
                    text = stringResource(R.string.email_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.InvalidEmail)) {
                Text(
                    text = stringResource(R.string.email_invalid),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { viewModel.handleScreenEvents(StoreReviewEvent.UpdatePhoneNumber(it)) },
                label = { Text(stringResource(R.string.phone_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyPhoneNumber) ||
                        state.validationErrors.contains(ValidationError.InvalidPhoneNumber)
            )
            if (state.validationErrors.contains(ValidationError.EmptyPhoneNumber)) {
                Text(
                    text = stringResource(R.string.phone_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.InvalidPhoneNumber)) {
                Text(
                    text = stringResource(R.string.phone_invalid),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.promotionalCode,
                onValueChange = { viewModel.handleScreenEvents(StoreReviewEvent.UpdatePromotionalCode(it)) },
                label = { Text(stringResource(R.string.promo_code_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyPromotionalCode) ||
                        state.validationErrors.contains(ValidationError.InvalidPromotionalCode)
            )
            if (state.validationErrors.contains(ValidationError.EmptyPromotionalCode)) {
                Text(
                    text = stringResource(R.string.promo_code_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.InvalidPromotionalCode)) {
                Text(
                    text = stringResource(R.string.promo_code_invalid),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.deliveryDate,
                onValueChange = { viewModel.handleScreenEvents(StoreReviewEvent.UpdateDeliveryDate(it)) },
                label = { Text(stringResource(R.string.delivery_date_label)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyDeliveryDate) ||
                        state.validationErrors.contains(ValidationError.InvalidDeliveryDate) ||
                        state.validationErrors.contains(ValidationError.MondayNotAllowed) ||
                        state.validationErrors.contains(ValidationError.FutureDateNotAllowed)
            )
            if (state.validationErrors.contains(ValidationError.EmptyDeliveryDate)) {
                Text(
                    text = stringResource(R.string.date_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.InvalidDeliveryDate)) {
                Text(
                    text = stringResource(R.string.date_invalid),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.MondayNotAllowed)) {
                Text(
                    text = stringResource(R.string.monday_not_allowed),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            } else if (state.validationErrors.contains(ValidationError.FutureDateNotAllowed)) {
                Text(
                    text = stringResource(R.string.future_date_not_allowed),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            RatingDropdown(
                selectedRating = state.rating,
                onRatingSelected = { viewModel.handleScreenEvents(StoreReviewEvent.UpdateRating(it)) },
                isError = state.validationErrors.contains(ValidationError.EmptyRating)
            )
            if (state.validationErrors.contains(ValidationError.EmptyRating)) {
                Text(
                    text = stringResource(R.string.rating_required),
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.handleScreenEvents(StoreReviewEvent.SubmitReview) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.send_evaluation))
            }

            if (state.isSubmitted) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.CheckCircle,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.evaluation_sent_success),
                            color = Color.Green,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingDropdown(
    selectedRating: Rating?,
    onRatingSelected: (Rating) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        val ratingLabel = stringResource(R.string.rating_label)
        OutlinedTextField(
            value = selectedRating?.let { stringResource(it.displayName) } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(text = ratingLabel) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            isError = isError
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Rating.values().forEach { rating ->
                DropdownMenuItem(
                    text = { Text(stringResource(rating.displayName)) },
                    onClick = {
                        onRatingSelected(rating)
                        expanded = false
                    }
                )
            }
        }
    }
}

val Rating.displayName: Int
    get() = when (this) {
        Rating.BAD -> R.string.rating_bad
        Rating.FINE -> R.string.rating_satisfactory
        Rating.GOOD -> R.string.rating_good
        Rating.GREAT -> R.string.rating_very_good
        Rating.EXCELLENT -> R.string.rating_excellent
    }

@Preview
@Composable
private fun StoreReviewScreenPreview() {
    EcommerceTheme {
        StoreReviewScreen(onBackClick = {})
    }
} 