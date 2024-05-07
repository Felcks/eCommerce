package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.Pokemon
import kotlinx.coroutines.flow.Flow

interface AddFavoritePokemonUseCase {
    suspend operator fun invoke(pokemon: Pokemon) : Flow<Boolean>
}