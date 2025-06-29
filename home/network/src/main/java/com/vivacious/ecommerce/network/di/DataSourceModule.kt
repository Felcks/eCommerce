package com.vivacious.ecommerce.network.di

import com.vivacious.ecommerce.domain.data_sources.ProductRemoteDataSource
import com.vivacious.ecommerce.network.api.ProductService
import com.vivacious.ecommerce.network.data_sources.ProductRemoteDataSourceImpl
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
    fun provideProductRemoteDataSource(
        productService: ProductService,
    ): ProductRemoteDataSource {
        return ProductRemoteDataSourceImpl(productService = productService)
    }
}