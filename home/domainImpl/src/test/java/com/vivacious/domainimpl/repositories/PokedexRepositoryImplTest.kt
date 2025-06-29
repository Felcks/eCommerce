package com.vivacious.domainimpl.repositories

import androidx.paging.PagingData
import app.cash.turbine.test
import com.vivacious.ecommerce.domain.data_sources.ProductRemoteDataSource
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.wrapper.Resource
import com.vivacious.ecommerce.domainimpl.repositories.ProductRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ProductRepositoryImplTest {

    private lateinit var mockPokedexRemoteDataSource: ProductRemoteDataSource
    private lateinit var sut : ProductRepositoryImpl

    @Before
    fun setup() {
        mockPokedexRemoteDataSource = mockk<ProductRemoteDataSource>()
        sut = ProductRepositoryImpl(mockPokedexRemoteDataSource, mockk())
    }

    @Test
    fun `GIVEN no parameters WHEN getProducts() THEN returns correctly` () = runTest {
        val expected = mockk<PagingData<ProductSummary>>()
        coEvery {  mockPokedexRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) } returns flowOf(expected)

        sut.getProducts().test {
            assertEquals(expected, awaitItem())
            coVerify(exactly = 1) { mockPokedexRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) }
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN error on dataSource WHEN getProducts() THEN throws error` () = runTest {
        val expected = Throwable()
        coEvery {  mockPokedexRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) } returns flow {
            throw expected
        }

        sut.getProducts().test {
            coVerify(exactly = 1) { mockPokedexRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) }
            assertEquals(expected, awaitError())
        }
    }

    @Test
    fun `GIVEN pokemonId WHEN getProduct() THEN returns correctly` () = runTest {
        val expected = mockk<Resource<Product>>()
        val pokemonId = "pokemonId"

        coEvery {  mockPokedexRemoteDataSource.getProduct(pokemonId) } returns flowOf(expected)

        sut.getProduct(pokemonId).test {
            assertEquals(expected, awaitItem())
            coVerify(exactly = 1) { mockPokedexRemoteDataSource.getProduct(pokemonId) }
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN error on dataSource WHEN getProduct() THEN throws error` () = runTest {
        val expected = Throwable()
        val pokemonId = "pokemonId"
        coEvery {  mockPokedexRemoteDataSource.getProduct(pokemonId) } returns flow {
            throw expected
        }

        sut.getProduct(pokemonId).test {
            coVerify(exactly = 1) { mockPokedexRemoteDataSource.getProduct(pokemonId) }
            assertEquals(expected, awaitError())
        }
    }
}