package tv.mechjack.platform.application;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;

public final class DefaultInjectorBridge implements InjectorBridge {

  final static DefaultInjectorBridge INSTANCE = new DefaultInjectorBridge();

  private Injector injector;

  DefaultInjectorBridge() {
    /* no-op (2018-12-23 mechjack) gives constructor package visibility */
  }

  void setInjector(final Injector injector) {
    this.injector = injector;
  }

  @Override
  public final <T> T getInstance(final Class<T> type) {
    return this.injector.getInstance(type);
  }

  @Override
  public <T> T getInstance(final Key<T> type) {
    return this.injector.getInstance(type);
  }

  @Override
  public final <T> Provider<T> getProvider(final Class<T> type) {
    return this.injector.getProvider(type);
  }

  @Override
  public <T> Provider<T> getProvider(final Key<T> type) {
    return this.injector.getProvider(type);
  }

}
