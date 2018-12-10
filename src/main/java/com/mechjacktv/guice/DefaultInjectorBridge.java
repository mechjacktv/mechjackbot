package com.mechjacktv.guice;

import com.google.inject.Injector;
import com.google.inject.Provider;

public class DefaultInjectorBridge implements InjectorBridge {

  final static DefaultInjectorBridge INSTANCE = new DefaultInjectorBridge();

  private Injector injector;

  void setInjector(final Injector injector) {
    this.injector = injector;
  }

  @Override
  public final <T> T getInstance(final Class<T> type) {
    return this.injector.getInstance(type);
  }

  @Override
  public final <T> Provider<T> getProvider(final Class<T> type) {
    return this.injector.getProvider(type);
  }

}
