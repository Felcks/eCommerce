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

    // Flag para controlar se já carregou produtos inicialmente
    private var hasLoadedInitialProducts = false
    
    // Flag para controlar se há uma busca ativa
    private var hasActiveSearch = false
    
    // Cache para os produtos normais
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
                // Não recarregar se já temos produtos
                if (products.value == PagingData.empty<ProductSummary>()) {
                    loadProducts()
                }
            }
            is HomeScreenEvent.SearchProducts -> {
                if (event.query.isEmpty()) {
                    // Se a query estiver vazia e havia uma busca ativa, voltar para produtos normais
                    if (hasActiveSearch) {
                        // Restaurar produtos do cache se disponível
                        cachedProducts?.let { cached ->
                            _products.value = cached
                        } ?: run {
                            hasLoadedInitialProducts = false // Reset para forçar carregamento
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
                    // Cache os produtos normais
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