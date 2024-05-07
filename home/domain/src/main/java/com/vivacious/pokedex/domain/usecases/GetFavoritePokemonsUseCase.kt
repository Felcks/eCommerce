package com.vivacious.pokedex.domain.usecases

import com.vivacious.pokedex.domain.models.Pokemon
import kotlinx.coroutines.flow.Flow

interface GetFavoritePokemonsUseCase {
    suspend operator fun invoke(): Flow<List<Pokemon>>
}