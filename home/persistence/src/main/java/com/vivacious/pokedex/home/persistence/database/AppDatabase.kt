package com.vivacious.pokedex.home.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vivacious.pokedex.home.persistence.dao.PokedexDao
import com.vivacious.pokedex.home.persistence.models.PokemonEntity

@Database(entities = [PokemonEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokedexDao(): PokedexDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE pokedex ADD COLUMN url TEXT NOT NULL DEFAULT a")
    }
}

