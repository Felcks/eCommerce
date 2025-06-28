package com.vivacious.pokedex.presentation.favoritelist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.domain.usecases.GetFavoriteProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val getFavoriteProductsUseCase: GetFavoriteProductsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<FavoriteListState> = MutableStateFlow(FavoriteListState())
    val state = _state.asStateFlow()

    fun handleScreenEvents(event: FavoriteListEvent) {
        when (event) {
            FavoriteListEvent.LoadFavoriteProducts -> loadFavoriteProducts()
        }
    }

    private fun loadFavoriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            getFavoriteProductsUseCase.invoke()
                .onStart {
                    _state.value = _state.value.copy(loading = true)
                }
                .catch {
                    _state.value = _state.value.copy(loading = false, errorMessage = it.message)
                }
                .collectLatest {
                    _state.value =
                        _state.value.copy(loading = false, errorMessage = null, products = it)
                }
        }
    }
}