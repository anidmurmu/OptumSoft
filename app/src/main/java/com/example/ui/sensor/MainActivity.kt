package com.example.ui.sensor

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.optumsoft.R
import com.example.optumsoft.databinding.ActivityMainBinding
import com.example.ui.utils.base.RVModelBindingAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.rvSensorList.adapter = RVModelBindingAdapter(
            emptyList(),
            viewModel,
            SensorVHFactory()
        )

        viewModel.getSensorList()
        viewModel.getSensorConfigList()

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(uiState: MainViewState) {
        if (uiState.hasConfigData) {
            viewModel.subscribeSensors()
        }
        if (uiState.hasSensorsSubscribed) {
            //viewModel.subscribeToSensorData()
            viewModel.subscribeToSingleSensorData()
            //viewModel.viewState.value.sensorGraphDataUiModel.sortedMap["temperature0"] = null
            Log.d("name1", viewModel.viewState.value.sensorGraphDataUiModel.toString())
            Log.d("name1", viewModel.viewState.value.sensorGraphDataUiModel.sortedMap.toString())
            viewModel.showGraphList(viewModel.viewState.value.sensorGraphDataUiModel.sortedMap,
            "temperature0")
        }
        if(uiState.valueInserted) {
            viewModel.showGraphList(viewModel.viewState.value.sensorGraphDataUiModel.sortedMap,
                "temperature0")
        }
    }
}