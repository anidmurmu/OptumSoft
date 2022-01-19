package com.example.domain.usecase.dummy.di

import com.example.domain.repository.dummy.DummyRepository
import com.example.domain.usecase.dummy.GetDummyDataUseCase
import com.example.domain.usecase.dummy.GetDummyDataUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class DummyUseCaseModule {

    @Provides
    @ActivityRetainedScoped
    fun provideGetDummyDataUseCase(
        dummyRepository: DummyRepository
    ): GetDummyDataUseCase {
        return GetDummyDataUseCaseImpl(dummyRepository)//dummyRepository)
    }
}