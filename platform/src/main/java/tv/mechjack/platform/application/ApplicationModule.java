package tv.mechjack.platform.application;

import com.google.inject.AbstractModule;

public final class ApplicationModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(InjectorBridge.class).toInstance(DefaultInjectorBridge.INSTANCE);
  }

}
