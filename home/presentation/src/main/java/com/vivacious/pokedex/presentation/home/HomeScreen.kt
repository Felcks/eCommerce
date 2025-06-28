package com.vivacious.pokedex.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.vivacious.pokedex.domain.models.ProductSummary
import com.vivacious.pokedex.core.presentation.theme.PokedexTheme
import com.vivacious.pokedex.presentation.R
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    goToProductDetail: (productId: String) -> Unit,
    goToFavoriteList: () -> Unit,
) {
    val products = homeScreenViewModel.products.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        if (products.itemCount == 0) {
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshProducts)
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.SearchProducts(searchQuery))
        } else {
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshProducts)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.home_scree_title),
                icon = {
                    IconButton(onClick = { goToFavoriteList() }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "View favorites",
                            modifier = modifier
                                .wrapContentSize()
                        )
                    }
                },
                modifier = Modifier.padding(top = 48.dp, start = 32.dp)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar produtos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            )

            when {
                products.loadState.refresh is LoadState.Loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                products.loadState.refresh is LoadState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            (products.loadState.refresh as LoadState.Error).error.message
                                ?: "Unexpected error",
                            style = TextStyle(textAlign = TextAlign.Center),
                            fontSize = 18.sp
                        )
                        Box(modifier = Modifier.padding(vertical = 8.dp))
                        Button(onClick = { homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshProducts) }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }

                products.itemCount > 0 -> {
                    ProductList(
                        products = products,
                        onProductClick = goToProductDetail,
                    )
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: LazyPagingItems<ProductSummary>,
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
            count = products.itemCount,
            key = products.itemKey { it.hashCode() },
        ) { index: Int ->
            val product = products[index]
            if (product != null) {
                ProductCard(
                    productSummary = product,
                    onProductClick = onProductClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun ProductListPreview() {
    MaterialTheme {
        ProductList(
            products = flowOf(PagingData.from(MOCK_PRODUCTS)).collectAsLazyPagingItems(),
            onProductClick = {})
    }
}

@Composable
fun ProductCard(
    productSummary: ProductSummary,
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .background(Color.White)
        .clickable {
            onProductClick.invoke(productSummary.id.toString())
        }) {
        Column {
            SubcomposeAsyncImage(
                model = productSummary.thumbnail,
                contentDescription = productSummary.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .heightIn(min = 150.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    productSummary.title,
                    fontSize = 16.sp,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                
                // Rating with custom icon based on rating value
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val ratingIcon = when {
                        productSummary.rating < 3 -> Icons.Filled.StarBorder
                        productSummary.rating < 4 -> Icons.Filled.StarHalf
                        else -> Icons.Default.Star
                    }
                    Icon(
                        imageVector = ratingIcon,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = String.format("%.1f", productSummary.rating),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductCardPreview() {
    PokedexTheme {
        ProductCard(MOCK_PRODUCTS.first(), onProductClick = {})
    }
}

@Composable
fun TopBar(title: String, icon: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 28.sp
        )
        icon()
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    PokedexTheme {
        TopBar(title = "E-commerce", {})
    }
}

val MOCK_PRODUCTS = listOf<ProductSummary>(
    object : ProductSummary {
        override val title: String = "iPhone 9"
        override val thumbnail: String = "https://dummyjson.com/image/i/products/1/thumbnail.jpg"
        override val images: List<String> = listOf("https://dummyjson.com/image/i/products/1/1.jpg")
        override val rating: Double = 4.69
        override val id: Int = 1
    },
    object : ProductSummary {
        override val title: String = "iPhone X"
        override val thumbnail: String = "https://dummyjson.com/image/i/products/2/thumbnail.jpg"
        override val images: List<String> = listOf("https://dummyjson.com/image/i/products/2/1.jpg")
        override val rating: Double = 4.44
        override val id: Int = 2
    }
)