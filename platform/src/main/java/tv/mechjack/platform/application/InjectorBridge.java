package tv.mechjack.platform.application;

import com.google.inject.Key;
import com.google.inject.Provider;

public interface InjectorBridge {

  static InjectorBridge getBridge() {
    return DefaultInjectorBridge.INSTANCE;
  }

  <T> T getInstance(Class<T> type);

  <T> T getInstance(Key<T> type);

  <T> Provider<T> getProvider(Class<T> type);

  <T> Provider<T> getProvider(Key<T> type);

}
