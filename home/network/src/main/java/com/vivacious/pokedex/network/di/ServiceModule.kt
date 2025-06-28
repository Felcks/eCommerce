package com.vivacious.pokedex.network.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    // Nenhum binding aqui, evitar duplicidade de ProductService
}