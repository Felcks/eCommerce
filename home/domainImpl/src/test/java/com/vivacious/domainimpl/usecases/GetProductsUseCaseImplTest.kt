package com.vivacious.domainimpl.usecases

import androidx.paging.PagingData
import app.cash.turbine.test
import com.vivacious.ecommerce.domain.models.ProductSummary
import com.vivacious.ecommerce.domain.repositories.ProductRepository
import com.vivacious.ecommerce.domainimpl.usecases.GetProductsUseCaseImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetProductsUseCaseImplTest {

    private lateinit var mockRepository : ProductRepository
    private lateinit var sut: GetProductsUseCaseImpl

    @Before
    fun setup() {
        mockRepository = mockk()
        sut = GetProductsUseCaseImpl(mockRepository)
    }

    @Test
    fun `GIVEN repository returns correct WHEN getProduct THEN returns success`() = runTest {
        val expected = mockk<PagingData<ProductSummary>>()
        coEvery { mockRepository.getProducts() } returns flowOf(expected)

        sut.invoke().test {
            coVerify(exactly = 1) { mockRepository.getProducts() }
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN repository throws error WHEN getProduct THEN returns error`() = runTest {
        val expected = Throwable()
        coEvery { mockRepository.getProducts() } returns flow {
            throw expected
        }

        sut.invoke().test {
            coVerify(exactly = 1) { mockRepository.getProducts() }
            assertEquals(expected, awaitError())
        }
    }
}