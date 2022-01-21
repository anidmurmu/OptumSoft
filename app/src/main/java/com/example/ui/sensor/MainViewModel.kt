package com.example.ui.sensor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import com.example.domain.usecase.sensor.GetSensorConfigListUseCase
import com.example.domain.usecase.sensor.GetSensorListUseCase
import com.example.ui.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getDummyDataUseCase: GetDummyDataUseCase,
    private val getSensorListUseCase: GetSensorListUseCase,
    private val getSensorConfigListUseCase: GetSensorConfigListUseCase
) : ViewModel() {

    init {
        getDummyData()
    }

    private val _viewState: MutableLiveData<MainViewState> =
        MutableLiveData(MainViewState.Initial)
    val viewState: LiveData<MainViewState> = _viewState

    fun getSensorList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getSensorListUseCase.getSensorList()
                .onFailure {
                    Log.e("apple1", (it as Exception).toString())
                }
                .onSuccess {
                    Log.d("apple2", it.toString())
                }
        }
    }

    fun getSensorConfigList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getSensorConfigListUseCase.getSensorConfigList()
                .onFailure {
                    Log.e("orange", (it as Exception).toString())
                }
                .onSuccess {
                    Log.d("orange", it.size.toString())
                }
        }
    }

    fun getDummyData() {
        var result = DummyUiModel("key", "errorResult")
        viewModelScope.launch(dispatcherProvider.io) {
            getDummyDataUseCase.getDummyData()
                .onSuccess {
                    result = result.copy(dummyValue = it.dummyValue)
                    _viewState.postValue(MainViewState.Success(result.dummyValue))
                }
                .onFailure {
                    Log.e("apple", it.toString())
                    _viewState.postValue(MainViewState.Failure)
                }
        }
    }
}