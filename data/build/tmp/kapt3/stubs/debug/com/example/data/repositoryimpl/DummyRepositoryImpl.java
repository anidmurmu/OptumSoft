package com.example.data.repositoryimpl;

import java.lang.System;

@kotlin.Metadata(mv = {1, 6, 0}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bH\u0016\u00f8\u0001\u0000\u00f8\u0001\u0001\u00f8\u0001\u0002\u00a2\u0006\u0004\b\n\u0010\u000bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000f\n\u0002\b\u0019\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\f"}, d2 = {"Lcom/example/data/repositoryimpl/DummyRepositoryImpl;", "Lcom/example/domain/repository/DummyRepository;", "dummySrc", "Lcom/example/data/source/dummy/DummySrc;", "dummyMapper", "Lcom/example/data/mapper/DummyMapper;", "(Lcom/example/data/source/dummy/DummySrc;Lcom/example/data/mapper/DummyMapper;)V", "getDummyData", "Lkotlin/Result;", "Lcom/example/domain/model/dummy/DummyUiModel;", "getDummyData-d1pmJ48", "()Ljava/lang/Object;", "data_debug"})
public final class DummyRepositoryImpl implements com.example.domain.repository.DummyRepository {
    private final com.example.data.source.dummy.DummySrc dummySrc = null;
    private final com.example.data.mapper.DummyMapper dummyMapper = null;
    
    @javax.inject.Inject()
    public DummyRepositoryImpl(@org.jetbrains.annotations.NotNull()
    com.example.data.source.dummy.DummySrc dummySrc, @org.jetbrains.annotations.NotNull()
    com.example.data.mapper.DummyMapper dummyMapper) {
        super();
    }
}