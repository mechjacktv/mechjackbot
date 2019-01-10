package tv.mechjack.util;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public final class UtilModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class).in(Scopes.SINGLETON);
    this.bind(ProtobufUtils.class).to(DefaultProtobufUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(DefaultTimeUtils.class).in(Scopes.SINGLETON);
  }

}
