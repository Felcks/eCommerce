package com.vivacious.pokedex.presentation.productreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vivacious.pokedex.domain.models.Rating
import com.vivacious.pokedex.domain.usecases.ValidationError

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductReviewScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProductReviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliação de Produto") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Nome do usuário
            OutlinedTextField(
                value = state.userName,
                onValueChange = { viewModel.handleScreenEvents(ProductReviewEvent.UpdateUserName(it)) },
                label = { Text("Nome do usuário") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyUserName)
            )
            if (state.validationErrors.contains(ValidationError.EmptyUserName)) {
                Text(
                    text = "Nome é obrigatório",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Email
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.handleScreenEvents(ProductReviewEvent.UpdateEmail(it)) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = state.validationErrors.contains(ValidationError.EmptyEmail) || 
                         state.validationErrors.contains(ValidationError.InvalidEmail)
            )
            when {
                state.validationErrors.contains(ValidationError.EmptyEmail) -> {
                    Text(
                        text = "Email é obrigatório",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.InvalidEmail) -> {
                    Text(
                        text = "Email inválido",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Número de telefone
            OutlinedTextField(
                value = state.phoneNumber,
                onValueChange = { 
                    // Apenas dígitos
                    val filtered = it.filter { char -> char.isDigit() }
                    viewModel.handleScreenEvents(ProductReviewEvent.UpdatePhoneNumber(filtered))
                },
                label = { Text("Número de telefone") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                isError = state.validationErrors.contains(ValidationError.EmptyPhoneNumber) || 
                         state.validationErrors.contains(ValidationError.InvalidPhoneNumber)
            )
            when {
                state.validationErrors.contains(ValidationError.EmptyPhoneNumber) -> {
                    Text(
                        text = "Número é obrigatório",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.InvalidPhoneNumber) -> {
                    Text(
                        text = "Apenas dígitos são permitidos",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Código promocional
            OutlinedTextField(
                value = state.promotionalCode,
                onValueChange = { 
                    // Apenas letras maiúsculas e hífens
                    val filtered = it.uppercase().filter { char -> char.isLetter() || char == '-' }
                    viewModel.handleScreenEvents(ProductReviewEvent.UpdatePromotionalCode(filtered))
                },
                label = { Text("Código promocional") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.contains(ValidationError.EmptyPromotionalCode) || 
                         state.validationErrors.contains(ValidationError.InvalidPromotionalCode)
            )
            when {
                state.validationErrors.contains(ValidationError.EmptyPromotionalCode) -> {
                    Text(
                        text = "Código promocional é obrigatório",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.InvalidPromotionalCode) -> {
                    Text(
                        text = "Apenas letras maiúsculas e hífens, 3-7 caracteres",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Data de entrega
            OutlinedTextField(
                value = state.deliveryDate,
                onValueChange = { viewModel.handleScreenEvents(ProductReviewEvent.UpdateDeliveryDate(it)) },
                label = { Text("Data de entrega (dd/MM/yyyy)") },
                modifier = Modifier.fillMaxWidth(),
                isError = state.validationErrors.any { 
                    it is ValidationError.EmptyDeliveryDate || 
                    it is ValidationError.InvalidDeliveryDate ||
                    it is ValidationError.MondayNotAllowed ||
                    it is ValidationError.FutureDateNotAllowed
                }
            )
            when {
                state.validationErrors.contains(ValidationError.EmptyDeliveryDate) -> {
                    Text(
                        text = "Data é obrigatória",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.InvalidDeliveryDate) -> {
                    Text(
                        text = "Data inválida (use dd/MM/yyyy)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.MondayNotAllowed) -> {
                    Text(
                        text = "Segunda-feira não é permitida",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                state.validationErrors.contains(ValidationError.FutureDateNotAllowed) -> {
                    Text(
                        text = "Data não pode estar no futuro",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Classificação (dropdown)
            RatingDropdown(
                selectedRating = state.rating,
                onRatingSelected = { viewModel.handleScreenEvents(ProductReviewEvent.UpdateRating(it)) },
                isError = state.validationErrors.contains(ValidationError.EmptyRating)
            )
            if (state.validationErrors.contains(ValidationError.EmptyRating)) {
                Text(
                    text = "Classificação é obrigatória",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão de envio
            Button(
                onClick = { viewModel.handleScreenEvents(ProductReviewEvent.SubmitReview) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text("Enviar Avaliação")
            }

            // Mensagem de sucesso
            if (state.isSubmitted) {
                Text(
                    text = "Avaliação enviada com sucesso!",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingDropdown(
    selectedRating: Rating?,
    onRatingSelected: (Rating) -> Unit,
    isError: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selectedRating?.displayName ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Classificação") },
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
                    text = { Text(rating.displayName) },
                    onClick = {
                        onRatingSelected(rating)
                        expanded = false
                    }
                )
            }
        }
    }
}

val Rating.displayName: String
    get() = when (this) {
        Rating.MAU -> "Mau"
        Rating.SATISFATORIO -> "Satisfatório"
        Rating.BOM -> "Bom"
        Rating.MUITO_BOM -> "Muito Bom"
        Rating.EXCELENTE -> "Excelente"
    } 