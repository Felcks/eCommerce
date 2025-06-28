package com.vivacious.pokedex.network.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ProductOkHttpClient

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class InterceptorLogging