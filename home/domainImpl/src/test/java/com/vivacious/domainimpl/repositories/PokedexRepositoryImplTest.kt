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

    private lateinit var mockProductRemoteDataSource: ProductRemoteDataSource
    private lateinit var sut : ProductRepositoryImpl

    @Before
    fun setup() {
        mockProductRemoteDataSource = mockk<ProductRemoteDataSource>()
        sut = ProductRepositoryImpl(mockProductRemoteDataSource, mockk())
    }

    @Test
    fun `GIVEN no parameters WHEN getProducts() THEN returns correctly` () = runTest {
        val expected = mockk<PagingData<ProductSummary>>()
        coEvery {  mockProductRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) } returns flowOf(expected)

        sut.getProducts().test {
            assertEquals(expected, awaitItem())
            coVerify(exactly = 1) { mockProductRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) }
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN error on dataSource WHEN getProducts() THEN throws error` () = runTest {
        val expected = Throwable()
        coEvery {  mockProductRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) } returns flow {
            throw expected
        }

        sut.getProducts().test {
            coVerify(exactly = 1) { mockProductRemoteDataSource.getProducts(ProductRepositoryImpl.PAGE_SIZE) }
            assertEquals(expected, awaitError())
        }
    }

    @Test
    fun `GIVEN productId WHEN getProduct() THEN returns correctly` () = runTest {
        val expected = mockk<Resource<Product>>()
        val productId = "productId"

        coEvery {  mockProductRemoteDataSource.getProduct(productId) } returns flowOf(expected)

        sut.getProduct(productId).test {
            assertEquals(expected, awaitItem())
            coVerify(exactly = 1) { mockProductRemoteDataSource.getProduct(productId) }
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN error on dataSource WHEN getProduct() THEN throws error` () = runTest {
        val expected = Throwable()
        val productId = "productId"
        coEvery {  mockProductRemoteDataSource.getProduct(productId) } returns flow {
            throw expected
        }

        sut.getProduct(productId).test {
            coVerify(exactly = 1) { mockProductRemoteDataSource.getProduct(productId) }
            assertEquals(expected, awaitError())
        }
    }
}