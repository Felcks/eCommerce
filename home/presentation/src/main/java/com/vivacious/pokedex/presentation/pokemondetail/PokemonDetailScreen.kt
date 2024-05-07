package com.vivacious.pokedex.presentation.pokemondetail

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.palette.graphics.Palette
import coil.compose.SubcomposeAsyncImage
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.core.presentation.theme.PokedexTheme
import com.vivacious.pokedex.presentation.R

@Composable
fun PokemonDetailScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE) {
        viewModel.handleScreenEvents(PokemonDetailEvent.LoadPokemon)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            when {
                state.loading -> {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.errorMessage != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            state.errorMessage
                                ?: stringResource(id = R.string.unexpected_error_message),
                            style = TextStyle(textAlign = TextAlign.Center),
                            fontSize = 18.sp
                        )
                        Box(modifier = Modifier.padding(vertical = 8.dp))
                        Button(onClick = { viewModel.handleScreenEvents(PokemonDetailEvent.LoadPokemon) }) {
                            Text(stringResource(id = R.string.try_again))
                        }
                    }
                }

                state.pokemon != null -> {
                    PokemonDetail(
                        pokemon = state.pokemon!!,
                        onAddFavoriteClick = { viewModel.handleScreenEvents(PokemonDetailEvent.AddPokemonAsFavorite) },
                        onBackClick = onBackClick
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetail(
    pokemon: Pokemon,
    onBackClick: () -> Unit,
    onAddFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var backgroundColor by remember { mutableStateOf(Color(147, 201, 172)) }

    Column {
        Box(
            modifier = modifier
                .clip(
                    CircleShape.copy(
                        bottomEnd = CornerSize(32.dp),
                        bottomStart = CornerSize(32.dp),
                        topStart = CornerSize(0.dp),
                        topEnd = CornerSize(0.dp)
                    )
                )
                .heightIn(min = 300.dp)
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 8.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onBackClick.invoke() },
                    modifier = modifier
                        .wrapContentSize()
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = modifier
                            .wrapContentSize()
                    )
                }
                Text(
                    "#${pokemon.id}",
                    fontSize = 22.sp,
                    color = Color.White,
                )
            }
            SubcomposeAsyncImage(
                model = pokemon.image,
                contentDescription = pokemon.name,
                contentScale = ContentScale.Inside,
                modifier = modifier
                    .heightIn(min = 300.dp)
                    .fillMaxWidth(),
                onSuccess = { result ->
                    val mutableBitmap = result.result.drawable.toBitmap(475, 475)
                        .copy(Bitmap.Config.RGBA_F16, false)
                    val palette = Palette.from(mutableBitmap).generate()
                    palette.swatches.firstOrNull()?.let {
                        backgroundColor = Color(it.rgb)
                    }
                },
            )
        }
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                pokemon.name.capitalize(Locale.current),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.padding(top = 8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (type in pokemon.types) {
                val color: String = elementColors[type.name] ?: "#777777"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(android.graphics.Color.parseColor(color)))
                        .padding(horizontal = 32.dp, vertical = 2.dp)
                ) {
                    Text(type.name, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text((pokemon.weight / 10.0).toString() + " KG", fontSize = 22.sp)
                Text(stringResource(id = R.string.weight))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text((pokemon.height / 10.0).toString() + " M", fontSize = 22.sp)
                Text(stringResource(id = R.string.height))
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 16.dp))
        Column(
            modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text("Base Stats", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.padding(vertical = 4.dp))
            for (status in pokemon.status) {
                StatusView(status.stat.name, status.baseStat)
            }
        }
        Button(onClick = { onAddFavoriteClick.invoke() }) {
            Text("Add as favorite")
        }
    }
}

@Composable
fun StatusView(name: String, value: Int, modifier: Modifier = Modifier) {
    val statReference = statMap[name] ?: StatusReference(name, 150, Color.Magenta)

    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        Box(modifier = Modifier.width(48.dp)) {
            Text(statReference.name.uppercase())
        }
        Spacer(modifier = Modifier.padding(horizontal = 8.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(Color.LightGray)
                .fillMaxWidth()
                .height(16.dp)
        ) {
            val percent = (value * 100) / statReference.maxValue
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(statReference.color)
                    .fillMaxWidth(fraction = percent * 0.01f)
                    .height(16.dp),
            )
            Text(
                "${value}/${statReference.maxValue}",
                color = Color.White,
                fontSize = 12.sp,
                textAlign = TextAlign.Start,
                style = TextStyle(
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    lineHeightStyle = LineHeightStyle(
                        alignment = LineHeightStyle.Alignment.Center,
                        trim = LineHeightStyle.Trim.None
                    )
                ),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .wrapContentHeight()
            )
        }
    }
}

@Preview
@Composable
private fun StatusViewPreview() {
    PokedexTheme {
        StatusView(name = "hp", value = 100)
    }
}

val statMap = mapOf(
    "hp" to StatusReference.HP(),
    "attack" to StatusReference.ATK(),
    "defense" to StatusReference.DEF(),
    "speed" to StatusReference.SPD(),
    "special-attack" to StatusReference.SATK(),
    "special-defense" to StatusReference.SDEF(),
)

open class StatusReference(val name: String, val maxValue: Int, val color: Color) {
    class HP : StatusReference("HP", 100, Color(214, 56, 69))
    class ATK : StatusReference("ATK", 150, Color(253, 166, 41))
    class DEF : StatusReference("DEF", 150, Color(0, 144, 234))
    class SPD : StatusReference("SPD", 150, Color(144, 175, 198))
    class SATK : StatusReference("SATK", 150, Color(157, 40, 155))
    class SDEF : StatusReference("SDEF", 150, Color(17, 68, 68))
}

val elementColors = mapOf(
    "normal" to "#A8A77A",
    "grass" to "#7AC74C",
    "fire" to "#EE8130",
    "water" to "#6390F0",
    "electric" to "#F7D02C",
    "grass" to "#7AC74C",
    "ice" to "#96D9D6",
    "fighting" to "#C22E28",
    "poison" to "#A33EA1",
    "ground" to "#E2BF65",
    "flying" to "#A98FF3",
    "psychic" to "#F95587",
    "bug" to "#A6B91A",
    "rock" to "#B6A136",
    "ghost" to "#735797",
    "dragon" to "#6F35FC",
    "dark:" to "#705746",
    "steel:" to "#B7B7CE",
    "fairy" to "#D685AD",

    )