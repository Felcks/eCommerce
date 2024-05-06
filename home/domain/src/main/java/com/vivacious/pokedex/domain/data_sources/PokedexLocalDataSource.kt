package com.vivacious.pokedex.domain.data_sources

import com.vivacious.pokedex.domain.models.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokedexLocalDataSource {
    fun savePokemon(pokemon: Pokemon) : Flow<Boolean>
}