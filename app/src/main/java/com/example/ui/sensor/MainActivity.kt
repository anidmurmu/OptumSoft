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
import com.example.optumsoft.R
import com.example.optumsoft.databinding.ActivityMainBinding
import com.example.ui.sensor.chart.setupGraph
import com.example.ui.sensor.chart.setupLineDataSet
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.scale_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
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
            viewModel.showGraphList(
                viewModel.viewState.value.sensorGraphDataUiModel.sortedMap,
                "temperature0"
            )
        }
        if (uiState.valueInserted) {
            /*viewModel.showGraphList(
                viewModel.viewState.value.sensorGraphDataUiModel.sortedMap,
                "temperature0"
            )*/
            val sensorReadingList = viewModel.initGraph(
                viewModel.viewState.value.sensorGraphDataUiModel.sortedMap,
                "temperature0"
            )
            val entryList = sensorReadingList?.mapIndexed { index, sensorReadingUiModel ->
                Log.d("entry1", sensorReadingUiModel.toString())
                Entry(index.toFloat(),
                    sensorReadingUiModel.sensorVal?.toFloat() ?: 0f)
            }
            /*val entryList = ArrayList<Entry>()
            for (i in 0..10) {
                entryList.add(Entry(i.toFloat(), i*i.toFloat()))
            }*/
            plotGraph(binding.lineChart, entryList)
        }
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