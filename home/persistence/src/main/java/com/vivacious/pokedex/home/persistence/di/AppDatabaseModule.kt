package com.vivacious.pokedex.home.persistence.di

import android.content.Context
import androidx.room.Room
import com.vivacious.pokedex.home.persistence.database.AppDatabase
import com.vivacious.pokedex.home.persistence.database.MIGRATION_1_2
import com.vivacious.pokedex.home.persistence.database.MIGRATION_2_3
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "pokedex-database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

}