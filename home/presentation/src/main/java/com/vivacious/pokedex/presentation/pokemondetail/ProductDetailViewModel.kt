package com.vivacious.pokedex.presentation.pokemondetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.usecases.GetProductUseCase
import com.vivacious.pokedex.domain.usecases.AddFavoriteProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
    private val addFavoriteProductUseCase: AddFavoriteProductUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> {
                loadProduct(event.productId)
            }
            ProductDetailEvent.AddProductAsFavorite -> {
                addProductAsFavorite()
            }
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = _state.value.copy(loading = true, errorMessage = null)
            
            getProductUseCase(productId).collect { result ->
                when (result) {
                    is com.vivacious.pokedex.domain.wrapper.Resource.Success -> {
                        _state.value = _state.value.copy(
                            loading = false,
                            product = result.data
                        )
                    }
                    is com.vivacious.pokedex.domain.wrapper.Resource.Error -> {
                        _state.value = _state.value.copy(
                            loading = false,
                            errorMessage = result.errorMessage
                        )
                    }
                    is com.vivacious.pokedex.domain.wrapper.Resource.Loading -> {
                        _state.value = _state.value.copy(loading = true)
                    }
                }
            }
        }
    }

    private fun addProductAsFavorite() {
        val currentProduct = _state.value.product
        if (currentProduct != null) {
            viewModelScope.launch(Dispatchers.IO) {
                addFavoriteProductUseCase(currentProduct).collect { success ->
                    // Handle success/failure if needed
                }
            }
        }
    }
}

data class ProductDetailState(
    val loading: Boolean = false,
    val product: Product? = null,
    val errorMessage: String? = null
) 