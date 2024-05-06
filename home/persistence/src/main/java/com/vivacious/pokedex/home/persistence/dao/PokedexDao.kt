package com.vivacious.pokedex.home.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vivacious.pokedex.home.persistence.models.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokedexDao {
    @Query("SELECT * FROM pokedex")
    fun getAll(): Flow<List<PokemonEntity>>

    @Insert
    suspend fun insertAll(vararg pokemons: PokemonEntity) : Unit
}