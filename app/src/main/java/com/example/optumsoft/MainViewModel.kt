package com.example.optumsoft

import androidx.lifecycle.ViewModel
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDummyDataUseCase: GetDummyDataUseCase
) : ViewModel() {

    fun getDummyData(): String {
        //return "this is dummy string"
        var result = DummyUiModel("key", "errorResult")
        getDummyDataUseCase.getDummyData().onSuccess {
            result = result.copy(dummyValue = it.dummyValue)
        }
        return result.dummyValue
    }
}