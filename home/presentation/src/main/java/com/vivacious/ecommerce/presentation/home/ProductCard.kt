package com.vivacious.ecommerce.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.core.presentation.theme.EcommerceTheme
import com.vivacious.ecommerce.core.presentation.theme.PriceGreen
import com.vivacious.ecommerce.core.presentation.theme.DiscountRed
import com.vivacious.ecommerce.core.presentation.theme.RatingBadRed
import com.vivacious.ecommerce.core.presentation.theme.RatingNeutralOrange
import com.vivacious.ecommerce.core.presentation.theme.RatingGoodGreen
import com.vivacious.ecommerce.presentation.R

@Composable
fun ProductCard(
    productSummary: Any, // Pode ser ProductSummary ou Product
    onProductClick: (productId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val id: String
    val title: String
    val thumbnail: String
    val price: Double
    val discountPercentage: Double
    val rating: Double
    when (productSummary) {
        is ProductSummary -> {
            id = productSummary.id.toString()
            title = productSummary.title
            thumbnail = productSummary.thumbnail
            price = productSummary.price
            discountPercentage = productSummary.discountPercentage
            rating = productSummary.rating
        }
        is Product -> {
            id = productSummary.id.toString()
            title = productSummary.title
            thumbnail = productSummary.thumbnail
            price = productSummary.price
            discountPercentage = productSummary.discountPercentage
            rating = productSummary.rating
        }
        else -> throw IllegalArgumentException(stringResource(R.string.unsupported_type))
    }
    Card(
        modifier = modifier
            .background(Color.White)
            .clickable { onProductClick.invoke(id) }
            .heightIn(min = 310.dp, max = 310.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            SubcomposeAsyncImage(
                model = thumbnail,
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .weight(1f)
            ) {
                Text(
                    title,
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
                        stringResource(R.string.price_format, price),
                        fontSize = 14.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = PriceGreen
                    )
                    if (discountPercentage > 0) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            stringResource(R.string.discount_format, discountPercentage),
                            fontSize = 12.sp,
                            color = DiscountRed,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
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
                    val (ratingIcon, ratingColor, ratingText) = when {
                        rating < 3 -> Triple(
                            Icons.Filled.ThumbDown,
                            RatingBadRed,
                            stringResource(R.string.bad_choice)
                        )
                        rating < 4 -> Triple(
                            Icons.AutoMirrored.Filled.StarHalf,
                            RatingNeutralOrange,
                            stringResource(R.string.neutral_choice)
                        )
                        else -> Triple(
                            Icons.Filled.ThumbUp,
                            RatingGoodGreen,
                            stringResource(R.string.great_choice)
                        )
                    }
                    Icon(
                        imageVector = ratingIcon,
                        contentDescription = ratingText,
                        tint = ratingColor,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp)
                    )
                    Text(
                        text = stringResource(R.string.rating_format, rating),
                        fontSize = 12.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium,
                        color = ratingColor
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = ratingText,
                        fontSize = 10.sp,
                        color = ratingColor,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductCardPreview() {
    EcommerceTheme {
        ProductCard(
            productSummary = object : ProductSummary {
                override val id: Int = 1
                override val title: String = "iPhone 9"
                override val thumbnail: String = "https://dummyjson.com/image/i/products/1/thumbnail.jpg"
                override val images: List<String> = listOf("https://dummyjson.com/image/i/products/1/1.jpg")
                override val rating: Double = 4.69
                override val price: Double = 549.0
                override val discountPercentage: Double = 12.96
            },
            onProductClick = {}
        )
    }
}

@Preview
@Composable
private fun ProductCardProductPreview() {
    EcommerceTheme {
        ProductCard(
            productSummary = object : Product {
                override val id: Int = 2
                override val title: String = "Produto Ruim"
                override val description: String = "Produto ruim para testar preview"
                override val price: Double = 199.0
                override val discountPercentage: Double = 0.0
                override val rating: Double = 2.1
                override val stock: Int = 10
                override val brand: String = "Marca"
                override val category: String = "Categoria"
                override val thumbnail: String = "https://dummyjson.com/image/i/products/3/thumbnail.jpg"
                override val images: List<String> = listOf("https://dummyjson.com/image/i/products/3/1.jpg")
            },
            onProductClick = {}
        )
    }
} 