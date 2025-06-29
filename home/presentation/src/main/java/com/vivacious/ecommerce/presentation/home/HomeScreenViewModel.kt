package com.vivacious.ecommerce.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.usecases.GetProductsUseCase
import com.vivacious.ecommerce.domain.usecases.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
) : ViewModel() {

    private val _products: MutableStateFlow<PagingData<ProductSummary>> = MutableStateFlow(PagingData.empty())
    val products = _products.asStateFlow()

    private var hasLoadedInitialProducts = false
    private var hasActiveSearch = false
    private var cachedProducts: PagingData<ProductSummary>? = null

    fun handleScreenEvents(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.GetFreshProducts -> {
                if (!hasLoadedInitialProducts) {
                    loadProducts()
                    hasLoadedInitialProducts = true
                    hasActiveSearch = false
                }
            }
            HomeScreenEvent.LoadMoreProducts -> {
                if (products.value == PagingData.empty<ProductSummary>()) {
                    loadProducts()
                }
            }
            is HomeScreenEvent.SearchProducts -> {
                if (event.query.isEmpty()) {
                    if (hasActiveSearch) {
                        cachedProducts?.let { cached ->
                            _products.value = cached
                        } ?: run {
                            hasLoadedInitialProducts = false
                            loadProducts()
                        }
                        hasActiveSearch = false
                    }
                } else {
                    searchProducts(event.query)
                    hasActiveSearch = true
                }
            }
        }
    }

    fun hasSearchQuery(): Boolean {
        return hasActiveSearch
    }

    private fun loadProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getProductsUseCase()
                .cachedIn(viewModelScope)
                .collectLatest {
                    _products.value = it

                    if (!hasActiveSearch) {
                        cachedProducts = it
                    }
                }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchProductsUseCase(query)
                .cachedIn(viewModelScope)
                .collectLatest { searchResults ->
                    _products.value = searchResults
                }
        }
    }
}