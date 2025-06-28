package com.vivacious.domainimpl.di

import com.vivacious.domainimpl.repositories.ProductRepositoryImpl
import com.vivacious.pokedex.domain.data_sources.ProductLocalDataSource
import com.vivacious.pokedex.domain.data_sources.ProductRemoteDataSource
import com.vivacious.pokedex.domain.repositories.ProductRepository
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
    fun provideProductRepository(
        productRemoteDataSource: ProductRemoteDataSource,
        productLocalDataSource: ProductLocalDataSource,
    ): ProductRepository {
        return ProductRepositoryImpl(productRemoteDataSource, productLocalDataSource)
    }
}