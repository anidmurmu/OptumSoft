package com.example.domain.usecase.dummy.di;

import java.lang.System;

@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0007\u00a8\u0006\u0005"}, d2 = {"Lcom/example/domain/usecase/dummy/di/DummyUseCaseModule;", "", "()V", "provideGetDummyDataUseCase", "Lcom/example/domain/usecase/dummy/GetDummyDataUseCase;", "domain_debug"})
@dagger.Module()
public final class DummyUseCaseModule {
    
    public DummyUseCaseModule() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.hilt.android.scopes.ActivityRetainedScoped()
    @dagger.Provides()
    public final com.example.domain.usecase.dummy.GetDummyDataUseCase provideGetDummyDataUseCase() {
        return null;
    }
}