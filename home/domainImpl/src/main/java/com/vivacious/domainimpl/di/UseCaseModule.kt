package com.vivacious.domainimpl.di

import com.vivacious.domainimpl.usecases.AddFavoritePokemonUseCaseImpl
import com.vivacious.domainimpl.usecases.GetPokemonUseCaseImpl
import com.vivacious.domainimpl.usecases.GetPokemonsUseCaseImpl
import com.vivacious.pokedex.domain.repositories.PokedexRepository
import com.vivacious.pokedex.domain.usecases.AddFavoritePokemonUseCase
import com.vivacious.pokedex.domain.usecases.GetPokemonUseCase
import com.vivacious.pokedex.domain.usecases.GetPokemonsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetPokemonsUseCase(
        repository: PokedexRepository,
    ): GetPokemonsUseCase {
        return GetPokemonsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetPokemonUseCase(
        repository: PokedexRepository,
    ): GetPokemonUseCase {
        return GetPokemonUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideAddFavoritePokemonUseCase(
        repository: PokedexRepository,
    ): AddFavoritePokemonUseCase {
        return AddFavoritePokemonUseCaseImpl(repository)
    }
}