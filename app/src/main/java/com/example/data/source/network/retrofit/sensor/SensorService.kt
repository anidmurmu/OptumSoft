package com.example.data.source.network.retrofit.sensor

import com.example.data.entity.sensor.SensorConfigNetworkModel
import retrofit2.http.GET

interface SensorService {
    @GET("sensornames/")
    suspend fun getSensorList(): List<String>

    @GET("config/")
    suspend fun getSensorConfigList(): List<SensorConfigNetworkModel>
}