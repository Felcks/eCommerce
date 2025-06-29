package com.vivacious.ecommerce.presentation.productdetail

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.StarHalf
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
import androidx.compose.ui.text.style.TextDecoration
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
import com.vivacious.ecommerce.core.presentation.theme.EcommerceTheme
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.presentation.R

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
                        isFavorite = state.isFavorite,
                        onToggleFavoriteClick = { viewModel.handleScreenEvents(ProductDetailEvent.ToggleFavorite) },
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
    isFavorite: Boolean,
    onToggleFavoriteClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backgroundColor by remember { mutableStateOf(Color(147, 201, 172)) }
    val minImageHeight = 80.dp
    val maxImageHeight = 440.dp
    val minImageHeightPx = with(LocalDensity.current) { minImageHeight.toPx() }
    val maxImageHeightPx = with(LocalDensity.current) { maxImageHeight.toPx() }
    val imageHeightPx = remember { mutableStateOf(maxImageHeightPx) }
    val lazyListState = rememberLazyListState()

    // NestedScroll to collapse/expand the image
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newHeight = imageHeightPx.value + delta
                // Scroll up collapses
                return if (delta < 0) {
                    val consumed = if (newHeight > minImageHeightPx) delta else minImageHeightPx - imageHeightPx.value
                    imageHeightPx.value = (imageHeightPx.value + consumed).coerceIn(minImageHeightPx, maxImageHeightPx)
                    Offset(0f, consumed)
                // Scroll down expands
                } else if (delta > 0) {
                    val consumed =
                        if (lazyListState.firstVisibleItemIndex == 0 && lazyListState.firstVisibleItemScrollOffset == 0) {
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
                    .padding(top = 48.dp, bottom = 32.dp)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp, start = 8.dp, end = 16.dp),
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
                        Text(
                            product.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            if (product.discountPercentage > 0) {
                                Text(
                                    "${String.format("%.2f", product.price)}€",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    textDecoration = TextDecoration.LineThrough,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    "${String.format("%.2f", product.price * (1 - product.discountPercentage / 100))}€",
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
                                    "${String.format("%.2f", product.price)}€",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2E7D32)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            val (ratingIcon, ratingColor, ratingText) = when {
                                product.rating < 3 -> Triple(
                                    Icons.Filled.ThumbDown,
                                    Color(0xFFD32F2F),
                                    "Má escolha"
                                )
                                product.rating < 4 -> Triple(
                                    Icons.AutoMirrored.Filled.StarHalf,
                                    Color(0xFFFF9800),
                                    "Escolha neutra"
                                )
                                else -> Triple(
                                    Icons.Filled.ThumbUp,
                                    Color(0xFF4CAF50),
                                    "Ótima escolha"
                                )
                            }
                            Icon(
                                imageVector = ratingIcon,
                                contentDescription = ratingText,
                                tint = ratingColor,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Text(
                                text = String.format("%.1f", product.rating),
                                fontSize = 16.sp,
                                color = ratingColor,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = ratingText,
                                fontSize = 12.sp,
                                color = ratingColor,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            "Estoque: ${product.stock} unidades",
                            fontSize = 16.sp,
                            color = if (product.stock > 0) Color(0xFF2E7D32) else Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Text(
                                "Marca: ${product.brand ?: "-"}",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                "Categoria: ${product.category}",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
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
                            onClick = { onToggleFavoriteClick.invoke() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                val (icon, text) = if(isFavorite) {
                                    Pair(Icons.Default.Bookmark, stringResource(R.string.remove_from_favorites))
                                } else {
                                    Pair(Icons.Default.BookmarkBorder, stringResource(R.string.add_to_favorites))
                                }

                                Icon(
                                    icon,
                                    contentDescription = "View favorites",
                                    modifier = modifier.wrapContentSize()
                                )
                                Text(
                                    text,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxHeight().padding(start = 16.dp)
                                )
                            }

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
    EcommerceTheme {
        ProductDetailScreen(
            onBackClick = {},
            productId = "1"
        )
    }
}

@Preview
@Composable
private fun ProductDetailPreview() {
    EcommerceTheme {
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
            isFavorite = false,
            onToggleFavoriteClick = {},
            onBackClick = {}
        )
    }
} 