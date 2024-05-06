package com.vivacious.domainimpl.di

import com.vivacious.domainimpl.repositories.PokedexRepositoryImpl
import com.vivacious.pokedex.domain.data_sources.PokedexLocalDataSource
import com.vivacious.pokedex.domain.data_sources.PokedexRemoteDataSource
import com.vivacious.pokedex.domain.repositories.PokedexRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePokedexRepositoru(
        pokedexRemoteDataSource: PokedexRemoteDataSource,
        pokedexLocalDataSource: PokedexLocalDataSource,
    ): PokedexRepository {
        return PokedexRepositoryImpl(pokedexRemoteDataSource, pokedexLocalDataSource)
    }
}