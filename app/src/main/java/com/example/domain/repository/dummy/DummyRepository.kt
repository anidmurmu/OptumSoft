package com.example.domain.repository.dummy

import com.example.domain.model.dummy.DummyUiModel

interface DummyRepository {
    suspend fun getDummyData(): Result<DummyUiModel>
}