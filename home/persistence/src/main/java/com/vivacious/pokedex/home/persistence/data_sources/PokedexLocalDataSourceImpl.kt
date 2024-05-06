package com.vivacious.pokedex.home.persistence.data_sources

import androidx.room.Room
import com.vivacious.pokedex.domain.data_sources.PokedexLocalDataSource
import com.vivacious.pokedex.domain.models.Pokemon
import com.vivacious.pokedex.home.persistence.database.AppDatabase
import com.vivacious.pokedex.home.persistence.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PokedexLocalDataSourceImpl(
    private val appDatabase: AppDatabase,
) : PokedexLocalDataSource {
    override fun savePokemon(pokemon: Pokemon): Flow<Boolean> {
        return flow {
            appDatabase.pokedexDao().insertAll(pokemon.toEntity())
            emit(true)
        }
    }
}