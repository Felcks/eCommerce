package com.vivacious.domainimpl.usecases

import app.cash.turbine.test
import com.vivacious.pokedex.domain.models.Product
import com.vivacious.pokedex.domain.repositories.ProductRepository
import com.vivacious.pokedex.domain.wrapper.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class getProductUseCaseImplTest {

    private lateinit var mockRepository: ProductRepository
    private lateinit var sut: GetProductUseCaseImpl

    @Before
    fun setup() {
        mockRepository = mockk()
        sut = GetProductUseCaseImpl(mockRepository)
    }

    @Test
    fun `GIVEN repository returns correct WHEN getProduct THEN returns success`() = runTest {
        val pokemonId = "pokemonId"
        val expected = mockk<Resource<Product>>()
        coEvery { mockRepository.getProduct(pokemonId) } returns flowOf(expected)

        sut.invoke(pokemonId).test {
            coVerify(exactly = 1) { mockRepository.getProduct(pokemonId) }
            assertEquals(expected, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `GIVEN repository throws error WHEN getProduct THEN returns error`() = runTest {
        val pokemonId = "pokemonId"
        val expected = Throwable()
        coEvery { mockRepository.getProduct(pokemonId) } returns flow {
            throw expected
        }

        sut.invoke(pokemonId).test {
            coVerify(exactly = 1) { mockRepository.getProduct(pokemonId) }
            assertEquals(expected, awaitError())
        }
    }
}