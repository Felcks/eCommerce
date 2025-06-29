package com.vivacious.ecommerce.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.usecases.GetProductUseCase
import com.vivacious.ecommerce.domain.usecases.AddFavoriteProductUseCase
import com.vivacious.ecommerce.domain.usecases.RemoveFavoriteProductUseCase
import com.vivacious.ecommerce.domain.usecases.IsProductFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addFavoriteProductUseCase: AddFavoriteProductUseCase,
    private val removeFavoriteProductUseCase: RemoveFavoriteProductUseCase,
    private val isProductFavoriteUseCase: IsProductFavoriteUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> {
                loadProduct(event.productId)
            }
            ProductDetailEvent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(loading = true, errorMessage = null)
            
            getProductUseCase(productId).collect { result ->
                when (result) {
                    is com.vivacious.ecommerce.domain.wrapper.Resource.Success -> {
                        val product = result.data
                        _state.value = _state.value.copy(
                            loading = false,
                            product = product
                        )
                        
                        // Verificar se o produto é favorito
                        product?.let { 
                            checkIfProductIsFavorite(it.id)
                        }
                    }
                    is com.vivacious.ecommerce.domain.wrapper.Resource.Error -> {
                        _state.value = _state.value.copy(
                            loading = false,
                            errorMessage = result.errorMessage
                        )
                    }
                    is com.vivacious.ecommerce.domain.wrapper.Resource.Loading -> {
                        _state.value = _state.value.copy(loading = true)
                    }
                }
            }
        }
    }

    private fun checkIfProductIsFavorite(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = isProductFavoriteUseCase(productId)
            _state.value = _state.value.copy(isFavorite = isFavorite)
        }
    }

    private fun toggleFavorite() {
        val currentProduct = _state.value.product
        val isCurrentlyFavorite = _state.value.isFavorite
        
        if (currentProduct != null) {
            viewModelScope.launch(Dispatchers.IO) {
                if (isCurrentlyFavorite) {
                    // Remover dos favoritos
                    removeFavoriteProductUseCase(currentProduct.id).collect { success ->
                        if (success) {
                            _state.value = _state.value.copy(isFavorite = false)
                        }
                    }
                } else {
                    // Adicionar aos favoritos
                    addFavoriteProductUseCase(currentProduct).collect { success ->
                        if (success) {
                            _state.value = _state.value.copy(isFavorite = true)
                        }
                    }
                }
            }
        }
    }
}

data class ProductDetailState(
    val loading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null,
    val isFavorite: Boolean = false
) 