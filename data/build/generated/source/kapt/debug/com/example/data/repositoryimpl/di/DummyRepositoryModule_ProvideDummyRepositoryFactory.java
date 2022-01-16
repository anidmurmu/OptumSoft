// Generated by Dagger (https://dagger.dev).
package com.example.data.repositoryimpl.di;

import com.example.data.mapper.DummyMapper;
import com.example.data.source.dummy.DummySrc;
import com.example.domain.repository.DummyRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class DummyRepositoryModule_ProvideDummyRepositoryFactory implements Factory<DummyRepository> {
  private final DummyRepositoryModule module;

  private final Provider<DummySrc> dummySrcProvider;

  private final Provider<DummyMapper> dummyMapperProvider;

  public DummyRepositoryModule_ProvideDummyRepositoryFactory(DummyRepositoryModule module,
      Provider<DummySrc> dummySrcProvider, Provider<DummyMapper> dummyMapperProvider) {
    this.module = module;
    this.dummySrcProvider = dummySrcProvider;
    this.dummyMapperProvider = dummyMapperProvider;
  }

  @Override
  public DummyRepository get() {
    return provideDummyRepository(module, dummySrcProvider.get(), dummyMapperProvider.get());
  }

  public static DummyRepositoryModule_ProvideDummyRepositoryFactory create(
      DummyRepositoryModule module, Provider<DummySrc> dummySrcProvider,
      Provider<DummyMapper> dummyMapperProvider) {
    return new DummyRepositoryModule_ProvideDummyRepositoryFactory(module, dummySrcProvider, dummyMapperProvider);
  }

  public static DummyRepository provideDummyRepository(DummyRepositoryModule instance,
      DummySrc dummySrc, DummyMapper dummyMapper) {
    return Preconditions.checkNotNullFromProvides(instance.provideDummyRepository(dummySrc, dummyMapper));
  }
}
