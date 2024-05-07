package com.vivacious.pokedex.presentation.pokemondetail

sealed class PokemonDetailEvent {
    object LoadPokemon : PokemonDetailEvent()
    object AddPokemonAsFavorite : PokemonDetailEvent()
}