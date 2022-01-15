package com.example.data.mapper

import com.example.data.entity.DummySrcModel
import com.example.domain.mapper.Mapper
import com.example.domain.model.dummy.DummyUiModel
import javax.inject.Inject

class DummyMapper @Inject constructor() :
    Mapper<Result<DummySrcModel>, Result<DummyUiModel>> {
    override fun mapFrom(inputModel: Result<DummySrcModel>): Result<DummyUiModel> {
        return inputModel.fold(
            onSuccess = { Result.success(mapToDummyUiModel(it)) },
            onFailure = { Result.failure(it) }
        )
    }

    private fun mapToDummyUiModel(srcModel: DummySrcModel): DummyUiModel {
        return srcModel.let {
            DummyUiModel(it.dummyKey, it.dummyValue)
        }
    }
}