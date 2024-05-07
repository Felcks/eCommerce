package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.domain.repositories.PokedexRepository
import com.vivacious.pokedex.domain.usecases.GetFavoritePokemonsUseCase
import kotlinx.coroutines.flow.Flow

class GetFavoritePokemonsUseCaseImpl(
    private val repository: PokedexRepository,
) : GetFavoritePokemonsUseCase {

    override suspend fun invoke(): Flow<List<Pokemon>> {
        return repository.getFavoritePokemons()
    }
}