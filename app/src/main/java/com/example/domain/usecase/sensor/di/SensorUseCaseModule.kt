package com.example.domain.usecase.sensor.di

import com.example.domain.repository.sensor.SensorRepository
import com.example.domain.usecase.sensor.GetSensorConfigListUseCase
import com.example.domain.usecase.sensor.GetSensorConfigListUseCaseImpl
import com.example.domain.usecase.sensor.GetSensorListUseCase
import com.example.domain.usecase.sensor.GetSensorListUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SensorUseCaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideGetSensorListUseCase(
        sensorRepository: SensorRepository
    ): GetSensorListUseCase {
        return GetSensorListUseCaseImpl(sensorRepository)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideGetSensorConfigListUseCase(
        sensorRepository: SensorRepository
    ): GetSensorConfigListUseCase {
        return GetSensorConfigListUseCaseImpl(sensorRepository)
    }
}