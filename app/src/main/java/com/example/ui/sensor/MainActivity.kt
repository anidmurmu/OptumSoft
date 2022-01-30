package com.example.ui.sensor

import android.os.Bundle
import android.util.Log
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
import com.example.ui.utils.base.RVModelBindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scale_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.scaleRecent -> {
                viewModel.setScaleTypeToRecent()
                //viewModel.showToast("recent is set")
                true
            }
            R.id.scaleMinute -> {
                viewModel.setScaleTypeToMinute()
                //viewModel.showToast("minute is set")
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    /*private fun handleState(uiState: MainViewState) {
        if (uiState.isSensorListShowing) {
            val sensorName = viewModel.getFirstSensorName()
            viewModel.subscribeToSensorData(sensorName)
            viewModel.subscribeToSensor(sensorName)
        }
        if (uiState.isSensorSubscribed) {
            //val sensorName = viewModel.getFirstSensorName()
            val sensorName = viewModel.getSubscribedSensorName()
            Log.d("subscribed12345", sensorName)
            val graphDataList = viewModel.getGraphData(sensorName)
            Log.d("apple1234", graphDataList.toString())
            val entryList = toEntryList(graphDataList)
            plotGraph(binding.lineChart, entryList)
        }
    }*/

    private fun handleState(uiState: MainViewState) {
        when(uiState.state) {
            State.Initial -> {
                viewModel.subscribeToSensorData1()
            }
            State.SensorConfigListShowing -> {
                val sensorName = viewModel.getFirstSensorName()
                //viewModel.subscribeToSensorData(sensorName)
                viewModel.subscribeToSensor(sensorName)
            }
            State.SensorSubscribed -> {
                val sensorName = viewModel.getSubscribedSensorName()
                Log.d("subscribed12345", sensorName)
                val graphDataList = viewModel.getGraphData(sensorName)
                Log.d("apple1234", graphDataList.toString())
                val entryList = toEntryList(graphDataList)
                plotGraph(binding.lineChart, entryList)
            }
            State.SensorUnsubscribed -> {

            }
            State.OnSubscriptionChange -> {
                val sensorName = viewModel.getSubscribedSensorName()
                Log.d("changeSub name", sensorName)
                val graphDataList = viewModel.getGraphData(sensorName)
                Log.d("changeSub list", graphDataList.toString())
                val entryList = toEntryList(graphDataList)
                plotGraph(binding.lineChart, entryList)
            }
            State.UpdateGraph -> {
                val sensorName = viewModel.getSubscribedSensorName()
                Log.d("changeSub name", sensorName)
                val graphDataList = viewModel.getGraphData(sensorName)
                Log.d("changeSub list", graphDataList.toString())
                val entryList = toEntryList(graphDataList)
                plotGraph(binding.lineChart, entryList)
            }
        }
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

        val lineDataSet = LineDataSet(entryList, "Dynamic Data").apply {
            axisDependency = YAxis.AxisDependency.LEFT
            lineWidth = 1f
            circleRadius = 2f
            valueTextSize = 5f
            valueTextColor = android.graphics.Color.BLACK
            color = android.graphics.Color.RED
            isHighlightEnabled = false
            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
        }

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet)

        val lineData = LineData(dataSets)
        chart.data = lineData
        chart.invalidate()
    }
}