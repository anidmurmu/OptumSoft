package com.example.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import com.example.ui.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getDummyDataUseCase: GetDummyDataUseCase
) : ViewModel() {

    init {
        getDummyData()
    }

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState.Initial)
    val viewState: StateFlow<MainViewState> = _viewState.asStateFlow()

    fun getDummyData() {
        var result = DummyUiModel("key", "errorResult")
        viewModelScope.launch(dispatcherProvider.io) {
            getDummyDataUseCase.getDummyData()
                .onSuccess {
                    result = result.copy(dummyValue = it.dummyValue)
                    _viewState.value = MainViewState.Success(result.dummyValue)
                }
                .onFailure {
                    Log.e("apple", it.toString())
                    _viewState.value = MainViewState.Failure
                }
        }
    }
}