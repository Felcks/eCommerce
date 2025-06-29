package com.vivacious.ecommerce.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vivacious.ecommerce.BuildConfig
import com.vivacious.ecommerce.presentation.favoritelist.favoriteList
import com.vivacious.ecommerce.presentation.favoritelist.navigateToFavoriteList
import com.vivacious.ecommerce.presentation.home.HomeScreenNavigationRoute
import com.vivacious.ecommerce.presentation.home.homeScreen
import com.vivacious.ecommerce.presentation.pokemondetail.navigateToProductDetail
import com.vivacious.ecommerce.presentation.pokemondetail.productDetail
import com.vivacious.ecommerce.presentation.storereview.navigateToStoreReview
import com.vivacious.ecommerce.presentation.storereview.storeReview

@Composable
fun MainNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeScreenNavigationRoute,
        modifier = modifier
    ) {
        homeScreen(
            goToProductDetail = {
                navController.navigateToProductDetail(productId = it)
            },
            goToFavoriteList = {
                navController.navigateToFavoriteList()
            },
            goToProductReview = {
                navController.navigateToStoreReview()
            },
            showStoreReviewForm = BuildConfig.SHOW_STORE_REVIEW_FORM
        )
        productDetail(onBackClick = { navController.popBackStack() })
        favoriteList(
            onBackClick = { navController.popBackStack() },
            goToProductDetail = {
                navController.navigateToProductDetail(productId = it)
            },
        )
        storeReview(onBackClick = { navController.popBackStack() })
    }
}