package com.example.domain.usecase.dummy

import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.repository.DummyRepository
import javax.inject.Inject

interface GetDummyDataUseCase {
    fun getDummyData(): Result<DummyUiModel>
}

class GetDummyDataUseCaseImpl @Inject constructor(
    //private val dummyRepository: DummyRepository
) : GetDummyDataUseCase {
    override fun getDummyData(): Result<DummyUiModel> {
        return Result.success(DummyUiModel("dummyKey", "dummyValue"))
        //return dummyRepository.getDummyData()
    }
}