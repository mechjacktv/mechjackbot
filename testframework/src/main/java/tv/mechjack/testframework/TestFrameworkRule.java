package tv.mechjack.testframework;

import java.lang.reflect.InvocationHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;

import org.junit.rules.ExternalResource;

import tv.mechjack.testframework.fake.FakeBuilder;
import tv.mechjack.testframework.fake.FakeFactory;

public final class TestFrameworkRule extends ExternalResource {

  public static final int ARBITRARY_COLLECTION_SIZE = 10;

  private final Set<Module> modules = new HashSet<>();
  private final TestClock testClock = new DefaultTestClock();
  private final TestRandom testRandom = new DefaultTestRandom();
  private Injector injector = null;

  @Override
  protected final void before() {
    this.modules.clear();
    this.testClock.reset();
    this.testRandom.reset();
    this.installModule(new TestFrameworkModule(this.testClock, this.testRandom));
  }

  @Override
  protected final void after() {
    this.injector = null;
  }

  public final void assertNullPointerException(final Throwable thrown, final String name) {
    this.getInstance(AssertionUtils.class).assertNullPointerException(thrown, name);
  }

  public final void currentTimeDelta(final long delta, final TimeUnit unit) {
    this.currentTimeDelta(delta, unit, 0);
  }

  public final void currentTimeDelta(final long delta, final TimeUnit unit, final long shift) {
    this.testClock.currentTimeDelta(unit.toMillis(delta) + shift);
  }

  public final <T> T fake(Class<T> type) {
    return this.getInstance(FakeFactory.class).fake(type);
  }

  public final <T> T fake(Class<T> type, InvocationHandler handler) {
    return this.getInstance(FakeFactory.class).fake(type, handler);
  }

  public final <T> FakeBuilder<T> fakeBuilder(Class<T> type) {
    return this.getInstance(FakeFactory.class).builder(type);
  }

  public final byte[] getArbitraryByteArray() {
    return this.getInstance(ArbitraryDataGenerator.class).getByteArray();
  }

  public final Integer getArbitraryInteger() {
    return this.getInstance(ArbitraryDataGenerator.class).getInteger();
  }

  public final Long getArbitraryLong() {
    return this.getInstance(ArbitraryDataGenerator.class).getLong();
  }

  public final String getArbitraryString() {
    return this.getInstance(ArbitraryDataGenerator.class).getString();
  }

  public final <T> T getInstance(final Class<T> type) {
    return this.getInjector().getInstance(type);
  }

  public final <T> T getInstance(final Key<T> type) {
    return this.getInjector().getInstance(type);
  }

  public final <T> Provider<T> getProvider(final Class<T> type) {
    return this.getInjector().getProvider(type);
  }

  public final <T> Provider<T> getProvider(final Key<T> type) {
    return this.getInjector().getProvider(type);
  }

  private Injector getInjector() {
    if (this.injector == null) {
      this.injector = Guice.createInjector(this.modules);
    }
    return this.injector;
  }

  public final void installModule(final Module module) {
    this.modules.add(module);
  }

  public final void nextRandomValue(final Long nextValue) {
    this.testRandom.setNextValue(nextValue);
  }

}
