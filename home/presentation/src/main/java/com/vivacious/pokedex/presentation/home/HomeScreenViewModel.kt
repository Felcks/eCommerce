package com.vivacious.pokedex.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.usecases.GetProductsUseCase
import com.vivacious.pokedex.domain.usecases.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
) : ViewModel() {

    private val _products: MutableStateFlow<PagingData<ProductSummary>> = MutableStateFlow(PagingData.empty())
    val products = _products.asStateFlow()

    private val _searchResults: MutableStateFlow<List<ProductSummary>> = MutableStateFlow(emptyList())
    val searchResults = _searchResults.asStateFlow()

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.GetFreshProducts -> {
                loadProducts()
            }
            HomeScreenEvent.LoadMoreProducts -> {
                loadProducts()
            }
            is HomeScreenEvent.SearchProducts -> {
                searchProducts(event.query)
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase().cachedIn(viewModelScope).collect {
                _products.value = it
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchProductsUseCase(query).collect { results ->
                _searchResults.value = results
            }
        }
    }
}