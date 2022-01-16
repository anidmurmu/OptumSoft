package com.example.data.repositoryimpl

import com.example.data.mapper.DummyMapper
import com.example.data.source.dummy.DummySrc
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.repository.DummyRepository
import javax.inject.Inject

class DummyRepositoryImpl @Inject constructor(
    private val dummySrc: DummySrc,
    private val dummyMapper: DummyMapper
) : DummyRepository {
    override suspend fun getDummyData(): Result<DummyUiModel> {
        return dummyMapper.mapFrom(dummySrc.getDummyData())
    }
}