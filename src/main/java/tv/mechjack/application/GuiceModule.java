package tv.mechjack.application;

import com.google.inject.AbstractModule;

public final class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(InjectorBridge.class).toInstance(DefaultInjectorBridge.INSTANCE);
  }

}
