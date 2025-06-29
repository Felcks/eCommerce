package com.vivacious.ecommerce.network.di

import android.content.Context
import com.vivacious.ecommerce.network.Constants.DUMMY_JSON_BASE_URL
import com.vivacious.ecommerce.network.api.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    @InterceptorLogging
    fun provideLoggingInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @ProductOkHttpClient
    fun provideOkHttpClient(
        @InterceptorLogging loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder().apply {
            addInterceptor(loggingInterceptor)
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            followRedirects(true)
            retryOnConnectionFailure(true)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    @ProductRetrofit
    fun provideRetrofit(
        @ProductOkHttpClient okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DUMMY_JSON_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductService(
        @ProductRetrofit retrofit: Retrofit
    ): ProductService {
        return retrofit.create(ProductService::class.java)
    }
}