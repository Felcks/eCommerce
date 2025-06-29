package com.vivacious.pokedex.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.ui.text.font.FontWeight
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    goToProductDetail: (productId: String) -> Unit,
    goToFavoriteList: () -> Unit,
    goToProductReview: () -> Unit,
) {
    val products = homeScreenViewModel.products.collectAsLazyPagingItems()
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    
    // Preservar o estado do scroll
    val gridState = rememberLazyGridState()

    // Só carregar produtos se não houver nenhum item carregado
    LaunchedEffect(Unit) {
        if (products.itemCount == 0) {
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshProducts)
        }
    }

    // Debounced search com 0,5 segundos
    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            isSearching = true
            delay(500) // 500ms debounce
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.SearchProducts(searchQuery))
            isSearching = false
        } else {
            // Se o campo estiver vazio, voltar para produtos normais imediatamente
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.SearchProducts(""))
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
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = goToProductReview,
                content = { Text("Avaliar produto") }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar produtos...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                trailingIcon = {
                    if (isSearching && searchQuery.isNotEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            )

            when {
                products.loadState.refresh is LoadState.Loading && products.itemCount == 0 -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                isSearching && searchQuery.isNotEmpty() -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Buscando por \"$searchQuery\"...",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
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
                        gridState = gridState
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            if (searchQuery.isNotEmpty()) "Nenhum produto encontrado para \"$searchQuery\"" else "Nenhum produto disponível",
                            style = TextStyle(textAlign = TextAlign.Center),
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: LazyPagingItems<ProductSummary>,
    onProductClick: (productId: String) -> Unit,
    gridState: LazyGridState = rememberLazyGridState(),
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = gridState,
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
                    modifier = Modifier.fillMaxWidth()
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
    Card(
        modifier = modifier
            .background(Color.White)
            .clickable {
                onProductClick.invoke(productSummary.id.toString())
            }
            .heightIn(min = 280.dp, max = 280.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = productSummary.thumbnail,
                contentDescription = productSummary.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(
                    productSummary.title,
                    fontSize = 14.sp,
                    maxLines = 2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 40.dp, max = 40.dp),
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 20.dp, max = 20.dp)
                ) {
                    Text(
                        "R$ ${String.format("%.2f", productSummary.price)}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    if (productSummary.discountPercentage > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "-${String.format("%.0f", productSummary.discountPercentage)}%",
                            fontSize = 12.sp,
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 20.dp, max = 20.dp)
                ) {
                    val ratingIcon = when {
                        productSummary.rating < 3 -> Icons.Filled.StarBorder
                        productSummary.rating < 4 -> Icons.Filled.StarHalf
                        else -> Icons.Default.Star
                    }
                    Icon(
                        imageVector = ratingIcon,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(16.dp)
                    )
                    Text(
                        text = String.format("%.1f", productSummary.rating),
                        fontSize = 12.sp
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
        override val price: Double = 549.0
        override val discountPercentage: Double = 12.96
    },
    object : ProductSummary {
        override val title: String = "iPhone X"
        override val thumbnail: String = "https://dummyjson.com/image/i/products/2/thumbnail.jpg"
        override val images: List<String> = listOf("https://dummyjson.com/image/i/products/2/1.jpg")
        override val rating: Double = 4.44
        override val id: Int = 2
        override val price: Double = 899.0
        override val discountPercentage: Double = 0.0
    }
)