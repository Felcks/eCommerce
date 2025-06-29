package com.vivacious.ecommerce.domainimpl.di

import com.vivacious.ecommerce.domainimpl.usecases.AddFavoriteProductUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.GetFavoriteProductsUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.GetProductUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.GetProductsUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.IsProductFavoriteUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.SearchProductsUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.ValidateStoreReviewUseCaseImpl
import com.vivacious.ecommerce.domainimpl.usecases.RemoveFavoriteProductUseCaseImpl
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.usecases.AddFavoriteProductUseCase
import com.vivacious.ecommerce.domain.usecases.GetFavoriteProductsUseCase
import com.vivacious.ecommerce.domain.usecases.GetProductUseCase
import com.vivacious.ecommerce.domain.usecases.GetProductsUseCase
import com.vivacious.ecommerce.domain.usecases.IsProductFavoriteUseCase
import com.vivacious.ecommerce.domain.usecases.RemoveFavoriteProductUseCase
import com.vivacious.ecommerce.domain.usecases.SearchProductsUseCase
import com.vivacious.ecommerce.domain.usecases.ValidateStoreReviewUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetProductsUseCase(
        repository: ProductRepository,
    ): GetProductsUseCase {
        return GetProductsUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetProductUseCase(
        repository: ProductRepository,
    ): GetProductUseCase {
        return GetProductUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideSearchProductsUseCase(
        repository: ProductRepository,
    ): SearchProductsUseCase {
        return SearchProductsUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideAddFavoriteProductUseCase(
        repository: ProductRepository,
    ): AddFavoriteProductUseCase {
        return AddFavoriteProductUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetFavoriteProductsUseCase(
        repository: ProductRepository,
    ): GetFavoriteProductsUseCase {
        return GetFavoriteProductsUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideRemoveFavoriteProductUseCase(
        repository: ProductRepository,
    ): RemoveFavoriteProductUseCase {
        return RemoveFavoriteProductUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideIsProductFavoriteUseCase(
        repository: ProductRepository,
    ): IsProductFavoriteUseCase {
        return IsProductFavoriteUseCaseImpl(repository)
    }

    @Provides
    @ViewModelScoped
    fun provideValidateProductReviewUseCase(): ValidateStoreReviewUseCase {
        return ValidateStoreReviewUseCaseImpl()
    }
}