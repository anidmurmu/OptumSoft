package com.example.ui.sensor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.dummy.DummyUiModel
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import com.example.domain.usecase.sensor.GetSensorConfigListUseCase
import com.example.domain.usecase.sensor.GetSensorListUseCase
import com.example.ui.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    //private val getDummyDataUseCase: GetDummyDataUseCase,
    private val getSensorListUseCase: GetSensorListUseCase,
    private val getSensorConfigListUseCase: GetSensorConfigListUseCase
) : ViewModel() {

    private val _viewState: MutableLiveData<MainViewState> =
        MutableLiveData()
    val viewState: LiveData<MainViewState> = _viewState

    init {
        //getDummyData()
        _viewState.value = MainViewState.Initial
    }

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
            val result = getSensorConfigListUseCase.getSensorConfigList()
            when (result) {
                is Response.Failure -> {
                    Log.e("orange", result.error.message.toString())
                }
                is Response.Success -> {
                    Log.i("orange success", result.data.toString())
                   //_viewState.value?.sensorConfigs?.clear()
                    Log.i("orange before", result.data.toString())
                    _viewState.value?.sensorConfigs?.value = result.data
                    //Log.i("orange size after adding", getSensors().size.toString())
                    //Log.i("orange list after adding", getSensors().toString())
                    updateViewState(MainViewState.HasConfigList(result.data))
                }
            }
        }
    }

    private fun updateViewState(state: MainViewState) {
        _viewState.postValue(state)
    }

    /*fun getDummyData() {
        var result = DummyUiModel("key", "errorResult")
        viewModelScope.launch(dispatcherProvider.io) {
            getDummyDataUseCase.getDummyData()
                .onSuccess {
                    result = result.copy(dummyValue = it.dummyValue)
                    //_viewState.postValue(MainViewState.State.Success(result.dummyValue))
                    _viewState.postValue(MainViewState.Success(result.dummyValue))
                }
                .onFailure {
                    Log.e("apple", it.toString())
                    _viewState.postValue(MainViewState.Failure)
                }
        }
    }*/

    fun getSensors(): List<SensorConfigUiModel> {
        return viewState.value?.sensorConfigs?.value ?: emptyList()
    }
}