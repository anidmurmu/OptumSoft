package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getDummyDataUseCase: GetDummyDataUseCase
) : ViewModel() {

    init {
        getDummyData()
    }

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState.Initial)
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    private fun getDummyData() {
        var result = DummyUiModel("key", "errorResult")
        viewModelScope.launch(Dispatchers.IO) {
            getDummyDataUseCase.getDummyData()
                .onSuccess {
                    result = result.copy(dummyValue = it.dummyValue)
                    _viewState.value = MainViewState.HasData(result.dummyValue)
                }
                .onFailure {
                    Log.e("apple", it.toString())
                }
        }
    }
}