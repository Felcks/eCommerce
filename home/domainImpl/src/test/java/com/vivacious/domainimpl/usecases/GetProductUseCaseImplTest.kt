package com.vivacious.domainimpl.usecases

import app.cash.turbine.test
import com.vivacious.ecommerce.domain.models.Product
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domain.wrapper.Resource
import com.vivacious.ecommerce.domainimpl.usecases.GetProductUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductUseCaseImplTest {

    private lateinit var mockRepository: ProductRepository
    private lateinit var sut: GetProductUseCaseImpl

    @Before
    fun setup() {
        mockRepository = mockk()
        sut = GetProductUseCaseImpl(mockRepository)
    }

    @Test
    fun `GIVEN repository returns correct WHEN getProduct THEN returns success`() = runTest {
        val productId = "productId"
        val expected = mockk<Resource<Product>>()
        coEvery { mockRepository.getProduct(productId) } returns flowOf(expected)

        sut.invoke(productId).test {
            coVerify(exactly = 1) { mockRepository.getProduct(productId) }
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN repository throws error WHEN getProduct THEN returns error`() = runTest {
        val productId = "productId"
        val expected = Throwable()
        coEvery { mockRepository.getProduct(productId) } returns flow {
            throw expected
        }

        sut.invoke(productId).test {
            coVerify(exactly = 1) { mockRepository.getProduct(productId) }
            assertEquals(expected, awaitError())
        }
    }
}