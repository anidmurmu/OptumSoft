package com.example.data.source.dummy

import com.example.data.entity.dummy.DummySrcModel
import javax.inject.Inject

class DummySrc @Inject constructor() {
    fun getDummyData(): Result<DummySrcModel> {
        val data = DummySrcModel("keyFromDataModule", "valueFromDataModule")
        return Result.success(data)
    }
}