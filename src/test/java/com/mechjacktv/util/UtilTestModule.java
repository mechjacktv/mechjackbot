package com.mechjacktv.util;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

import com.mechjacktv.testframework.NullMessageForNameFactory;

public class UtilTestModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).in(Scopes.SINGLETON);
    this.bind(NullMessageForNameFactory.class).to(ExecutionUtilsNullMessageForNameFactory.class).in(Scopes.SINGLETON);
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
    this.bind(TestTimeUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(TestTimeUtils.class);
  }

}
