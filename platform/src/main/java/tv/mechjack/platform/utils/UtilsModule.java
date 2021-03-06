package tv.mechjack.platform.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public final class UtilsModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class)
        .to(DefaultExecutionUtils.class)
        .in(Scopes.SINGLETON);

    this.bind(RandomUtils.class)
        .to(DefaultRandomUtils.class)
        .in(Scopes.SINGLETON);

    this.bind(TimeUtils.class)
        .to(DefaultTimeUtils.class)
        .in(Scopes.SINGLETON);
  }

}
