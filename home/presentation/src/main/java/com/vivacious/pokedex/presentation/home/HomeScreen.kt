package com.vivacious.pokedex.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.vivacious.pokedex.domain.models.PokemonSummary
import com.vivacious.pokedex.core.presentation.theme.PokedexTheme
import com.vivacious.pokedex.presentation.R
import kotlinx.coroutines.flow.flowOf

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
    goToPokemonDetail: (pokemonUrl: String) -> Unit,
    goToFavoriteList: () -> Unit,
) {
    val pokemons = homeScreenViewModel.pokemons.collectAsLazyPagingItems()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        if (pokemons.itemCount == 0) {
            homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshPokemons)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.home_scree_title),
                icon = {
                    IconButton(onClick = { goToFavoriteList() }) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "View favorites",
                            modifier = modifier
                                .wrapContentSize()
                        )
                    }
                },
                modifier = Modifier.padding(top = 48.dp, start = 32.dp)
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when {
                pokemons.loadState.refresh is LoadState.Loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                pokemons.loadState.refresh is LoadState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            (pokemons.loadState.refresh as LoadState.Error).error.message
                                ?: "Unexpected error",
                            style = TextStyle(textAlign = TextAlign.Center),
                            fontSize = 18.sp
                        )
                        Box(modifier = Modifier.padding(vertical = 8.dp))
                        Button(onClick = { homeScreenViewModel.handleScreenEvents(HomeScreenEvent.GetFreshPokemons) }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }


                pokemons.itemCount > 0 -> {
                    PokemonList(
                        pokemons = pokemons,
                        onPokemonClick = goToPokemonDetail,
                    )
                }
            }
        }
    }
}


@Composable
fun PokemonList(
    pokemons: LazyPagingItems<PokemonSummary>,
    onPokemonClick: (pokemonUrl: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            count = pokemons.itemCount,
            key = pokemons.itemKey { it.hashCode() },
        ) { index: Int ->
            val pokemon = pokemons[index]
            if (pokemon != null) {
                PokemonCard(
                    pokemonSummary = pokemon,
                    onPokemonClick = onPokemonClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
private fun PokemonListPreview() {
    MaterialTheme {
        PokemonList(
            pokemons = flowOf(PagingData.from(MOCK_POKEDEX)).collectAsLazyPagingItems(),
            onPokemonClick = {})
    }
}

@Composable
fun PokemonCard(
    pokemonSummary: PokemonSummary,
    onPokemonClick: (pokemonUrl: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier
        .background(Color.White)
        .clickable {
            onPokemonClick.invoke(pokemonSummary.url)
        }) {
        Column {
            SubcomposeAsyncImage(
                model = pokemonSummary.image,
                contentDescription = pokemonSummary.name,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .clip(CircleShape.copy(all = CornerSize(16.dp)))
                    .heightIn(min = 150.dp)
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    pokemonSummary.name.capitalize(Locale.current),
                    fontSize = 22.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

        }
    }
}

@Preview
@Composable
private fun PokemonCardPreview() {
    PokedexTheme {
        PokemonCard(MOCK_POKEDEX.first(), onPokemonClick = {})
    }
}

@Composable
fun TopBar(title: String, icon: @Composable () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 28.sp
        )
        icon()
    }

}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    PokedexTheme {
        TopBar(title = "Pokedex", {})
    }
}


val MOCK_POKEDEX = listOf<PokemonSummary>(
    object : PokemonSummary {
        override val name: String
            get() = "bulbasaur"
        override val url: String
            get() = "https://pokeapi.co/api/v2/pokemon/1/"
        override val image: String
            get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
    },
    object : PokemonSummary {
        override val name: String
            get() = "charmander"
        override val url: String
            get() = "https://pokeapi.co/api/v2/pokemon/4/"
        override val image: String
            get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/4.png"
    }
)