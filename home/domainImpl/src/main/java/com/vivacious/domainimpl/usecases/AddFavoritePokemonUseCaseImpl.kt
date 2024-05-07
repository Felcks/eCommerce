package com.vivacious.domainimpl.usecases

import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.domain.repositories.PokedexRepository
import com.vivacious.pokedex.domain.usecases.AddFavoritePokemonUseCase
import kotlinx.coroutines.flow.Flow

class AddFavoritePokemonUseCaseImpl(
    private val repository: PokedexRepository,
) : AddFavoritePokemonUseCase {

    override suspend fun invoke(pokemon: Pokemon): Flow<Boolean> {
        return repository.savePokemonAsFavorite(pokemon)
    }
}