package com.mechjacktv.guice;

import java.util.Objects;

import com.google.inject.Injector;
import com.google.inject.Provider;

public class InjectorBridge {

  static InjectorBridge INSTANCE = null;

  private final Injector injector;

  public static InjectorBridge getInstance() {
    if (Objects.isNull(INSTANCE)) {
      throw new IllegalStateException("No InjectorBridge instance has been set.");
    }
    return INSTANCE;
  }

  InjectorBridge(final Injector injector) {
    this.injector = injector;
  }

  public final <T> T getInstance(Class<T> type) {
    return this.injector.getInstance(type);
  }

  public final <T> Provider<T> getProvider(Class<T> type) {
    return this.injector.getProvider(type);
  }

}
