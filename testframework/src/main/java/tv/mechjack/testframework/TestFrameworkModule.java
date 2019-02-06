package tv.mechjack.testframework;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.OptionalBinder;

import tv.mechjack.testframework.fake.DefaultFakeFactory;
import tv.mechjack.testframework.fake.FakeFactory;

public final class TestFrameworkModule extends AbstractModule {

  private final TestClock testClock;
  private final TestRandom testRandom;

  TestFrameworkModule(final TestClock testClock, final TestRandom testRandom) {
    this.testClock = testClock;
    this.testRandom = testRandom;
  }

  @Override
  protected final void configure() {
    this.bind(ArbitraryDataGenerator.class).in(Scopes.SINGLETON);
    this.bind(AssertionUtils.class).in(Scopes.SINGLETON);
    this.bind(TestClock.class).toInstance(this.testClock);
    this.bind(FakeFactory.class).to(DefaultFakeFactory.class);
    this.bind(TestRandom.class).toInstance(this.testRandom);

    OptionalBinder.newOptionalBinder(this.binder(), NullMessageForNameFactory.class);
  }

}
