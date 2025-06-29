package com.vivacious.pokedex.presentation.productreview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ProductReviewNavigationRoute = "product_review_navigation"

fun NavController.navigateToProductReview(
    navOptions: NavOptions? = null
) {
    this.navigate(ProductReviewNavigationRoute, navOptions)
}

fun NavGraphBuilder.productReview(
    onBackClick: () -> Unit,
) {
    composable(
        ProductReviewNavigationRoute
    ) {
        ProductReviewScreen(
            onBackClick = onBackClick,
        )
    }
} 