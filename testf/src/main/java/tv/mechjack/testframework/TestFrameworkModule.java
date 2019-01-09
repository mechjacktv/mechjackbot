package tv.mechjack.testframework;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.OptionalBinder;

public final class TestFrameworkModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.bind(ArbitraryDataGenerator.class).in(Scopes.SINGLETON);
    this.bind(AssertionUtils.class).in(Scopes.SINGLETON);
    this.bind(TestClock.class).in(Scopes.SINGLETON);

    OptionalBinder.newOptionalBinder(this.binder(), NullMessageForNameFactory.class);
  }

}
