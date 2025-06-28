package com.vivacious.pokedex.presentation.favoritelist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.presentation.R
import com.vivacious.pokedex.presentation.home.TopBar
import com.vivacious.pokedex.presentation.home.ProductCard

@Composable
fun FavoriteListScreen(
    onBackClick: () -> Unit,
    goToProductDetail: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleScreenEvents(FavoriteListEvent.LoadFavoriteProducts)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = stringResource(id = R.string.favorite_list_screen_title),
                icon = { },
                modifier = Modifier.padding(top = 48.dp, start = 32.dp)
            )
        },

        ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when {
                state.loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            state.errorMessage
                                ?: stringResource(id = R.string.unexpected_error_message),
                            style = TextStyle(textAlign = TextAlign.Center),
                            fontSize = 18.sp
                        )
                        Box(modifier = Modifier.padding(vertical = 8.dp))
                        Button(onClick = { viewModel.handleScreenEvents(FavoriteListEvent.LoadFavoriteProducts) }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }

                state.products != null -> {
                    ProductList(
                        products = state.products!!,
                        onProductClick = goToProductDetail,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            count = products.size
        ) { index: Int ->
            val product = products[index]
            ProductCard(
                productSummary = product,
                onProductClick = { onProductClick(product.id.toString()) },
                modifier = Modifier
            )
        }
    }
}