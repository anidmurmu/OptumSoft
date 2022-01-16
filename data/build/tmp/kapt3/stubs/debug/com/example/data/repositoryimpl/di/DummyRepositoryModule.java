package com.example.data.repositoryimpl.di;

import java.lang.System;

@dagger.hilt.InstallIn(value = {dagger.hilt.android.components.ActivityRetainedComponent.class})
@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a8\u0006\t"}, d2 = {"Lcom/example/data/repositoryimpl/di/DummyRepositoryModule;", "", "()V", "provideDummyRepository", "Lcom/example/domain/repository/DummyRepository;", "dummySrc", "Lcom/example/data/source/dummy/DummySrc;", "dummyMapper", "Lcom/example/data/mapper/DummyMapper;", "data_debug"})
@dagger.Module()
public final class DummyRepositoryModule {
    
    public DummyRepositoryModule() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    @dagger.hilt.android.scopes.ActivityRetainedScoped()
    @dagger.Provides()
    public final com.example.domain.repository.DummyRepository provideDummyRepository(@org.jetbrains.annotations.NotNull()
    com.example.data.source.dummy.DummySrc dummySrc, @org.jetbrains.annotations.NotNull()
    com.example.data.mapper.DummyMapper dummyMapper) {
        return null;
    }
}