package com.vivacious.pokedex.presentation.favoritelist

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable

const val FavoriteListNavigationRoute = "favorite_list_navigation_route"

fun NavController.navigateToFavoriteList(
    navOptions: NavOptions? = null
) {
    this.navigate(FavoriteListNavigationRoute, navOptions)
}

fun NavGraphBuilder.favoriteList(
    onBackClick: () -> Unit
) {
    composable(
        FavoriteListNavigationRoute,
    ) {
        FavoriteListScreen(
            onBackClick
        )
    }
}