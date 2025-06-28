package com.vivacious.pokedex.presentation.pokemondetail

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import com.vivacious.pokedex.core.presentation.theme.PokedexTheme
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.presentation.R

@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    productId: String,
    modifier: Modifier = Modifier,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleScreenEvents(ProductDetailEvent.LoadProduct(productId))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
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
                        Button(onClick = { viewModel.handleScreenEvents(ProductDetailEvent.LoadProduct(productId)) }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }

                state.product != null -> {
                    ProductDetailWithCollapsingToolbar(
                        product = state.product!!,
                        onAddFavoriteClick = { viewModel.handleScreenEvents(ProductDetailEvent.AddProductAsFavorite) },
                        onBackClick = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
fun ProductDetailWithCollapsingToolbar(
    product: Product,
    onBackClick: () -> Unit,
    onAddFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backgroundColor by remember { mutableStateOf(Color(147, 201, 172)) }
    val minImageHeight = 80.dp
    val maxImageHeight = 400.dp
    val minImageHeightPx = with(LocalDensity.current) { minImageHeight.toPx() }
    val maxImageHeightPx = with(LocalDensity.current) { maxImageHeight.toPx() }
    val imageHeightPx = remember { mutableStateOf(maxImageHeightPx) }
    val lazyListState = rememberLazyListState()

    // NestedScroll para colapsar a imagem
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = imageHeightPx.value + delta
                return if (delta < 0) { // Scroll para cima, colapsa
                    val consumed = if (newHeight > minImageHeightPx) delta else minImageHeightPx - imageHeightPx.value
                    imageHeightPx.value = (imageHeightPx.value + consumed).coerceIn(minImageHeightPx, maxImageHeightPx)
                    Offset(0f, consumed)
                } else if (delta > 0) { // Scroll para baixo, expande
                    val consumed = if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
                        if (newHeight < maxImageHeightPx) delta else maxImageHeightPx - imageHeightPx.value
                    } else 0f
                    imageHeightPx.value = (imageHeightPx.value + consumed).coerceIn(minImageHeightPx, maxImageHeightPx)
                    Offset(0f, consumed)
                } else {
                    Offset.Zero
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Imagem do produto colapsável com fundo colorido
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { imageHeightPx.value.toDp() })
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .zIndex(1f)
        ) {
            SubcomposeAsyncImage(
                model = product.images.firstOrNull() ?: product.thumbnail,
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 32.dp)
                    .clip(
                        CircleShape.copy(
                            bottomEnd = CornerSize(32.dp),
                            bottomStart = CornerSize(32.dp),
                            topStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp)
                        )
                    ),
                onSuccess = { result ->
                    val mutableBitmap = result.result.drawable.toBitmap(475, 475)
                        .copy(Bitmap.Config.RGBA_F16, false)
                    val palette = Palette.from(mutableBitmap).generate()
                    palette.swatches.firstOrNull()?.let {
                        backgroundColor = Color(it.rgb)
                    }
                },
            )
            // Top bar sobreposta
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onBackClick.invoke() },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier.wrapContentSize()
                    )
                }
                Text(
                    "#${product.id}",
                    fontSize = 22.sp,
                    color = Color.White,
                )
            }
        }
        
        // Conteúdo principal com fundo branco
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = with(LocalDensity.current) { imageHeightPx.value.toDp() })
                .nestedScroll(nestedScrollConnection)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(
                                topStart = 32.dp,
                                topEnd = 32.dp
                            )
                        )
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Title
                        Text(
                            product.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Price and Discount
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            if (product.discountPercentage > 0) {
                                Text(
                                    "R$ ${String.format("%.2f", product.price)}",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    "R$ ${String.format("%.2f", product.price * (1 - product.discountPercentage / 100))}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "-${String.format("%.0f", product.discountPercentage)}%",
                                    fontSize = 16.sp,
                                    color = Color.Red,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    "R$ ${String.format("%.2f", product.price)}",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = String.format("%.1f", product.rating),
                                fontSize = 16.sp
                            )
                        }
                        // Stock
                        Text(
                            "Estoque: ${product.stock} unidades",
                            fontSize = 16.sp,
                            color = if (product.stock > 0) Color(0xFF2E7D32) else Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // Brand and Category
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                "Marca: ${product.brand}",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Categoria: ${product.category}",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        // Description
                        Text(
                            "Descrição:",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            product.description,
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        Spacer(modifier = Modifier.padding(vertical = 16.dp))
                        Button(
                            onClick = { onAddFavoriteClick.invoke() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Adicionar aos favoritos")
                        }
                        Spacer(modifier = Modifier.padding(vertical = 32.dp))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductDetailScreenPreview() {
    PokedexTheme {
        ProductDetailScreen(
            onBackClick = {},
            productId = "1"
        )
    }
}

@Preview
@Composable
private fun ProductDetailPreview() {
    PokedexTheme {
        ProductDetailWithCollapsingToolbar(
            product = object : Product {
                override val id: Int = 1
                override val title: String = "iPhone 9"
                override val description: String = "An apple mobile which is nothing like apple"
                override val price: Double = 549.0
                override val discountPercentage: Double = 12.96
                override val rating: Double = 4.69
                override val stock: Int = 94
                override val brand: String = "Apple"
                override val category: String = "smartphones"
                override val thumbnail: String = "https://dummyjson.com/image/i/products/1/thumbnail.jpg"
                override val images: List<String> = listOf("https://dummyjson.com/image/i/products/1/1.jpg")
            },
            onBackClick = {},
            onAddFavoriteClick = {}
        )
    }
} 