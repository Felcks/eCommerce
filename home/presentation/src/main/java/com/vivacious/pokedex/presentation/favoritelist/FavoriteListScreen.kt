package com.vivacious.pokedex.presentation.favoritelist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
import coil.compose.SubcomposeAsyncImage
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

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
                        androidx.compose.material3.CircularProgressIndicator()
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
                    if (state.products!!.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "Nenhum produto favorito adicionado",
                                style = TextStyle(textAlign = TextAlign.Center),
                                fontSize = 18.sp
                            )
                        }
                    } else {
                        ProductList(
                            products = state.products!!,
                            onProductClick = goToProductDetail,
                            onRemoveFavorite = { productId ->
                                viewModel.handleScreenEvents(FavoriteListEvent.RemoveFavoriteProduct(productId))
                            },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductList(
    products: List<Product>,
    onProductClick: (productId: String) -> Unit,
    onRemoveFavorite: (productId: Int) -> Unit,
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
            FavoriteProductCard(
                product = product,
                onProductClick = { onProductClick(product.id.toString()) },
                onRemoveFavorite = { onRemoveFavorite(product.id) },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun FavoriteProductCard(
    product: Product,
    onProductClick: (productId: String) -> Unit,
    onRemoveFavorite: (productId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .background(Color.White)
            .clickable {
                onProductClick.invoke(product.id.toString())
            }
            .heightIn(min = 320.dp, max = 320.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                SubcomposeAsyncImage(
                    model = product.thumbnail,
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
                
                // Botão de remover favorito
                IconButton(
                    onClick = { onRemoveFavorite(product.id) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .background(Color.Red.copy(alpha = 0.8f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Remover dos favoritos",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(
                    product.title,
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
                        "${String.format("%.2f", product.price)}€",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    if (product.discountPercentage > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "-${String.format("%.0f", product.discountPercentage)}%",
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
                        product.rating < 3 -> Icons.Filled.StarBorder
                        product.rating < 4 -> Icons.Filled.StarHalf
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
                        text = String.format("%.1f", product.rating),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}