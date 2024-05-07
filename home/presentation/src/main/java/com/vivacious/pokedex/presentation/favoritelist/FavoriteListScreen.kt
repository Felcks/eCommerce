package com.vivacious.pokedex.presentation.favoritelist

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteListScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteListViewModel = hiltViewModel(),
) {
    Text("This is the favorite List")
}