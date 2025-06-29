package com.vivacious.ecommerce.presentation.storereview

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val StoreReviewNavigationRoute = "store_review_navigation"

fun NavController.navigateToStoreReview(
    navOptions: NavOptions? = null
) {
    this.navigate(StoreReviewNavigationRoute, navOptions)
}

fun NavGraphBuilder.storeReview(
    onBackClick: () -> Unit,
) {
    composable(
        StoreReviewNavigationRoute
    ) {
        StoreReviewScreen(
            onBackClick = onBackClick,
        )
    }
} 