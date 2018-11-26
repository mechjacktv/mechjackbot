package com.mechjacktv.guice;

import com.google.inject.Injector;
import com.google.inject.Provider;

public class InjectorBridge {

  private final static InjectorBridge INSTANCE = new InjectorBridge();

  private Injector injector;

  public static InjectorBridge getBridge() {
    return INSTANCE;
  }

  void setInjector(final Injector injector) {
    this.injector = injector;
  }

  public final <T> T getInstance(final Class<T> type) {
    return this.injector.getInstance(type);
  }

  public final <T> Provider<T> getProvider(final Class<T> type) {
    return this.injector.getProvider(type);
  }

}
