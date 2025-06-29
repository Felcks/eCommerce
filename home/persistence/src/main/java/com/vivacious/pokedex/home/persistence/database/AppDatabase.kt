package com.vivacious.pokedex.home.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vivacious.pokedex.home.persistence.dao.PokedexDao
import com.vivacious.pokedex.home.persistence.dao.ProductDao
import com.vivacious.pokedex.home.persistence.models.PokemonEntity
import com.vivacious.pokedex.home.persistence.models.ProductEntity

@Database(entities = [PokemonEntity::class, ProductEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pokedexDao(): PokedexDao
    abstract fun productDao(): ProductDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE pokedex ADD COLUMN url TEXT NOT NULL DEFAULT a")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("""
            CREATE TABLE favorite_products (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                price REAL NOT NULL,
                discountPercentage REAL NOT NULL,
                rating REAL NOT NULL,
                stock INTEGER NOT NULL,
                brand TEXT NOT NULL,
                category TEXT NOT NULL,
                thumbnail TEXT NOT NULL,
                images TEXT NOT NULL
            )
        """)
    }
}

