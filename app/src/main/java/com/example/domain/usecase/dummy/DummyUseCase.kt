package com.example.domain.usecase.dummy

import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.repository.dummy.DummyRepository
import javax.inject.Inject

interface GetDummyDataUseCase {
    suspend fun getDummyData(): Result<DummyUiModel>
}

class GetDummyDataUseCaseImpl @Inject constructor(
    private val dummyRepository: DummyRepository
) : GetDummyDataUseCase {
    override suspend fun getDummyData(): Result<DummyUiModel> {
        return dummyRepository.getDummyData()
    }
}