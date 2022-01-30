package com.example.ui.sensor

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.sensor.SensorReadingUiModel
import com.example.optumsoft.R
import com.example.optumsoft.databinding.ActivityMainBinding
import com.example.ui.sensor.chart.setupGraph
import com.example.ui.sensor.chart.setupLineDataSet
import com.example.ui.utils.base.RVModelBindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


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

        setupGraph(binding.lineChart)

        binding.rvSensorList.adapter = RVModelBindingAdapter(
            emptyList(),
            viewModel,
            SensorVHFactory()
        )

        viewModel.getSensorNameList()
        viewModel.getSensorConfigList()

        lifecycleScope.launchWhenResumed {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.viewState.collect {
                    handleState(it)
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.graphUpdate.collectLatest {
                updateGraphData()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scale_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.scaleRecent -> {
                viewModel.setScaleTypeToRecent()
                true
            }
            R.id.scaleMinute -> {
                viewModel.setScaleTypeToMinute()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun handleState(uiState: MainViewState) {
        when (uiState.state) {
            State.Initial -> {
                viewModel.subscribeToSensorData()
            }
            State.SensorConfigListShowing -> {
                val sensorName = viewModel.getFirstSensorName()
                viewModel.subscribeToSensor(sensorName)
            }
            State.SensorSubscribed -> {
                updateGraphData()
            }
            State.SensorUnsubscribed -> {

            }
            State.OnSubscriptionChange -> {
                updateGraphData()
            }
            State.UpdateGraph -> {
                updateGraphData()
            }
        }
    }


    private fun updateGraphData() {
        val sensorName = viewModel.getSubscribedSensorName()
        val graphDataList = viewModel.getGraphData(sensorName)
        val entryList = toEntryList(graphDataList)
        plotGraph(binding.lineChart, entryList)
    }

    private fun toEntryList(sensorReadingList: List<SensorReadingUiModel>?): List<Entry>? {
        val entryList = sensorReadingList?.mapIndexed { index, sensorReadingUiModel ->
            Entry(
                index.toFloat(),
                sensorReadingUiModel.sensorVal?.toFloat() ?: 0f
            )
        }
        return entryList
    }

    private fun plotGraph(chart: LineChart, entryList: List<Entry>?) {
        setupLineDataSet(chart, entryList)
    }
}