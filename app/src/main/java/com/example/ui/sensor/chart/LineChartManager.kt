package com.example.ui.sensor.chart

import com.example.optumsoft.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

fun setupGraph(chart: LineChart) {
    chart.apply {
        description.isEnabled = true
        description.text = "Realtime data"
        setTouchEnabled(false)
        isDragEnabled = false
        setScaleEnabled(false)
        setDrawGridBackground(false)
        setPinchZoom(false)
        setBackgroundColor(resources.getColor(R.color.white))

        val lineData = LineData()
        lineData.setValueTextColor(resources.getColor(R.color.white))
        data = lineData

        setXAxis(chart)
        setLegend(chart)
    }
}

fun setupLineDataSet(chart: LineChart, entryList: List<Entry>?) {
    val lineDataSet = LineDataSet(entryList, "Dynamic Data").apply {
        axisDependency = YAxis.AxisDependency.LEFT
        lineWidth = 1f
        circleRadius = 3f
        valueTextSize = 7f
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

fun setMinValYLeftAxis(chart: LineChart, minVal: Float = 0f) {
    chart.axisLeft.apply {
        axisMinimum = minVal
    }
}

fun setMaxValYLeftAxis(chart: LineChart, maxVal: Float = 100f) {
    chart.axisLeft.apply {
        axisMaximum = maxVal
    }
}

fun setXAxis(chart: LineChart) {
    chart.xAxis.apply {
        textColor = android.graphics.Color.BLACK
        setDrawGridLines(false)
        setAvoidFirstLastClipping(true)
        isEnabled = true
    }

    chart.axisLeft.apply {
        textColor = android.graphics.Color.BLACK
        setDrawGridLines(false)
        setMaxValYLeftAxis(chart)
        setMinValYLeftAxis(chart)
    }

    chart.axisRight.apply {
        isEnabled = false
    }
}

fun setLegend(chart: LineChart) {
    chart.legend.apply {
        form = Legend.LegendForm.LINE
        textColor = android.graphics.Color.BLACK
    }
}