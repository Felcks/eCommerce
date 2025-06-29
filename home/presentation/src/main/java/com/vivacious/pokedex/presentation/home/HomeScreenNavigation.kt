package com.vivacious.pokedex.presentation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val HomeScreenNavigationRoute = "home_screen_navigation"

fun NavController.navigateToHomeScreen(
    navOptions: NavOptions? = null
) {
    this.navigate(HomeScreenNavigationRoute, navOptions)
}

fun NavGraphBuilder.homeScreen(
    goToProductDetail: (productId: String) -> Unit,
    goToFavoriteList: () -> Unit,
    goToProductReview: () -> Unit,
) {
    composable(
        HomeScreenNavigationRoute
    ) {
        HomeScreen(
            goToProductDetail = goToProductDetail,
            goToFavoriteList = goToFavoriteList,
            goToProductReview = goToProductReview
        )
    }
}