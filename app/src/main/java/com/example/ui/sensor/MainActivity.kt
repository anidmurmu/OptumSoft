package com.example.ui.sensor

import android.os.Bundle
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
            viewModel.subscribeToSensorData()
        }
    }
}