package tv.mechjack.platform.utils;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TestUtilsModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ExecutionUtils.class).to(DefaultExecutionUtils.class)
        .in(Scopes.SINGLETON);
    this.bind(TestRandomUtils.class).in(Scopes.SINGLETON);
    this.bind(RandomUtils.class).to(TestRandomUtils.class);
    this.bind(TestTimeUtils.class).in(Scopes.SINGLETON);
    this.bind(TimeUtils.class).to(TestTimeUtils.class);
  }

}
