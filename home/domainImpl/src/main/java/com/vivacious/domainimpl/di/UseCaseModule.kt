package com.vivacious.domainimpl.di

import com.vivacious.domainimpl.usecases.AddFavoriteProductUseCaseImpl
import com.vivacious.domainimpl.usecases.GetFavoriteProductsUseCaseImpl
import com.vivacious.domainimpl.usecases.GetProductUseCaseImpl
import com.vivacious.domainimpl.usecases.GetProductsUseCaseImpl
import com.vivacious.domainimpl.usecases.SearchProductsUseCaseImpl
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.usecases.AddFavoriteProductUseCase
import com.vivacious.pokedex.domain.usecases.GetFavoriteProductsUseCase
import com.vivacious.pokedex.domain.usecases.GetProductUseCase
import com.vivacious.pokedex.domain.usecases.GetProductsUseCase
import com.vivacious.pokedex.domain.usecases.SearchProductsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetProductsUseCase(
        repository: ProductRepository,
    ): GetProductsUseCase {
        return GetProductsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetProductUseCase(
        repository: ProductRepository,
    ): GetProductUseCase {
        return GetProductUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideSearchProductsUseCase(
        repository: ProductRepository,
    ): SearchProductsUseCase {
        return SearchProductsUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideAddFavoriteProductUseCase(
        repository: ProductRepository,
    ): AddFavoriteProductUseCase {
        return AddFavoriteProductUseCaseImpl(repository)
    }

    @Provides
    @Singleton
    fun provideGetFavoriteProductsUseCase(
        repository: ProductRepository,
    ): GetFavoriteProductsUseCase {
        return GetFavoriteProductsUseCaseImpl(repository)
    }
}