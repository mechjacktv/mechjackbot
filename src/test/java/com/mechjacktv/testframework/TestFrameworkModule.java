package com.mechjacktv.testframework;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestFrameworkModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ArbitraryDataGenerator.class).in(Scopes.SINGLETON);
  }

}
