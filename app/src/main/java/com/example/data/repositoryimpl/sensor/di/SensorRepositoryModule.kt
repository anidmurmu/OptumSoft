package com.example.data.repositoryimpl.sensor.di

import com.example.data.mapper.sensor.SensorConfigMapper
import com.example.data.repositoryimpl.sensor.SensorRepositoryImpl
import com.example.data.source.network.retrofit.sensor.SensorService
import com.example.domain.repository.sensor.SensorRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SensorRepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideSensorRepository(
        sensorService: SensorService,
        sensorConfigMapper: SensorConfigMapper
    ): SensorRepository {
        return SensorRepositoryImpl(sensorService, sensorConfigMapper)
    }
}