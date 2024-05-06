package com.vivacious.pokedex.domain.repositories

import androidx.paging.PagingData
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.domain.models.PokemonSummary
import com.vivacious.pokedex.domain.wrapper.Resource
import kotlinx.coroutines.flow.Flow

interface PokedexRepository {
    suspend fun getPokemons(): Flow<PagingData<PokemonSummary>>
    suspend fun getPokemon(pokemonId: String): Flow<Resource<Pokemon?>>
    suspend fun savePokemonAsFavorite(pokemon: Pokemon) : Flow<Boolean>
}