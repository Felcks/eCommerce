package com.vivacious.pokedex.presentation.pokemondetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val ProductDetailNavigationRoute = "product_detail/{productId}"

fun NavController.navigateToProductDetail(productId: String, navOptions: NavOptions? = null) {
    this.navigate("product_detail/$productId", navOptions)
}

fun NavGraphBuilder.productDetail(onBackClick: () -> Unit) {
    composable(
        route = ProductDetailNavigationRoute
    ) { backStackEntry ->
        val productId = backStackEntry.arguments?.getString("productId") ?: "1"
        ProductDetailScreen(
            onBackClick = onBackClick,
            productId = productId
        )
    }
} 