package com.example.domain.usecase.dummy

import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.repository.DummyRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetDummyDataUseCaseImplTest {

    private lateinit var dummyRepository: DummyRepository
    private lateinit var sut: GetDummyDataUseCase

    @Before
    fun setUp() {
        dummyRepository = mockk()
        sut = GetDummyDataUseCaseImpl(dummyRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getDummyData_returnsSuccess() = runTest {
        // given
        val uiModel = DummyUiModel("testKey", "testValue")
        coEvery { dummyRepository.getDummyData() } returns Result.success(uiModel)

        // when
        val result = sut.getDummyData()

        // then
        Truth.assertThat(result).isEqualTo(Result.success(uiModel))
    }

    @Test
    fun getDummyData_returnsFailure() = runTest {
        // given
        val exception = Exception()
        coEvery { dummyRepository.getDummyData() } returns Result.failure(exception)

        // when
        val result = sut.getDummyData()

        // then
        Truth.assertThat(result).isEqualTo(Result.failure<Exception>(exception))
    }
}