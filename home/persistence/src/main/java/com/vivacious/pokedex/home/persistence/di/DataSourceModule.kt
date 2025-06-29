package com.vivacious.pokedex.home.persistence.di

import com.vivacious.pokedex.domain.data_sources.ProductLocalDataSource
import com.vivacious.pokedex.home.persistence.data_sources.ProductLocalDataSourceImpl
import com.vivacious.pokedex.home.persistence.database.AppDatabase
import com.vivacious.pokedex.home.persistence.mappers.ProductMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    
    @Provides
    @Singleton
    fun provideProductMapper(): ProductMapper {
        return ProductMapper()
    }
    
    @Provides
    @Singleton
    fun provideProductLocalDataSource(
        appDatabase: AppDatabase,
        productMapper: ProductMapper
    ): ProductLocalDataSource {
        return ProductLocalDataSourceImpl(appDatabase, productMapper)
    }
}