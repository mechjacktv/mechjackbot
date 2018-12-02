package com.mechjacktv.util;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class UtilTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(DefaultTimeUtils.class).in(Scopes.SINGLETON);
  }
}
