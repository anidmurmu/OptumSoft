package com.example.data.entity.sensor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SensorNetworkModel(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("recent")
    @Expose
    val recentList: List<SensorReadingNetworkModel>?,

    @SerializedName("minute")
    @Expose
    val minuteList: List<SensorReadingNetworkModel>?,

    @SerializedName("key")
    @Expose
    val sensorKey: String?,

    @SerializedName("val")
    @Expose
    val sensorVal: String?,

    @SerializedName("sensor")
    @Expose
    val sensorName: String?,

    @SerializedName("scale")
    @Expose
    val scale: String?,
)

data class SensorReadingNetworkModel(
    @SerializedName("key")
    @Expose
    val sensorKey: String?,

    @SerializedName("val")
    @Expose
    val sensorVal: String?
)

data class SensorRangeNetworkModel(
    @SerializedName("min")
    @Expose
    val min: Int,

    @SerializedName("max")
    @Expose
    val max: Int
)