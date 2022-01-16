package com.example.domain.repository

import com.example.domain.model.dummy.DummyUiModel

interface DummyRepository {
    fun getDummyData(): Result<DummyUiModel>
}