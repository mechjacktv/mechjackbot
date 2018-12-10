package com.mechjacktv.guice;

import com.google.inject.Provider;

public interface InjectorBridge {

  static InjectorBridge getBridge() {
    return DefaultInjectorBridge.INSTANCE;
  }

  <T> T getInstance(Class<T> type);

  <T> Provider<T> getProvider(Class<T> type);

}
