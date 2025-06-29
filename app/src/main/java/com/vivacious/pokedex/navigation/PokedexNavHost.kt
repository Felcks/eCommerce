package com.vivacious.pokedex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vivacious.pokedex.presentation.favoritelist.favoriteList
import com.vivacious.pokedex.presentation.favoritelist.navigateToFavoriteList
import com.vivacious.pokedex.presentation.home.HomeScreenNavigationRoute
import com.vivacious.pokedex.presentation.home.homeScreen
import com.vivacious.pokedex.presentation.pokemondetail.navigateToProductDetail
import com.vivacious.pokedex.presentation.pokemondetail.productDetail
import com.vivacious.pokedex.presentation.productreview.navigateToProductReview
import com.vivacious.pokedex.presentation.productreview.productReview

@Composable
fun PokedexNavHost(modifier: Modifier = Modifier) {
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
                navController.navigateToProductReview()
            }
        )
        productDetail(onBackClick = { navController.popBackStack() })
        favoriteList(
            onBackClick = { navController.popBackStack() },
            goToProductDetail = {
                navController.navigateToProductDetail(productId = it)
            },
        )
        productReview(onBackClick = { navController.popBackStack() })
    }
}