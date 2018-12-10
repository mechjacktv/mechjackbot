package com.mechjacktv.guice;

import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(InjectorBridge.class).toInstance(DefaultInjectorBridge.INSTANCE);
  }

}
