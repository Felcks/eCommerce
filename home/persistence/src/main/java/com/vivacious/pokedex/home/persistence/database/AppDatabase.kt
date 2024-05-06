package com.vivacious.pokedex.home.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vivacious.pokedex.home.persistence.dao.PokedexDao
import com.vivacious.pokedex.home.persistence.models.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokedexDao(): PokedexDao
}