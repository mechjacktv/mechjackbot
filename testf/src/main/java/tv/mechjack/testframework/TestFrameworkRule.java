package tv.mechjack.testframework;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;

import org.junit.rules.ExternalResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TestFrameworkRule extends ExternalResource {

  public static final int ARBITRARY_COLLECTION_SIZE = 10;

  private final Logger logger = LoggerFactory.getLogger(TestFrameworkRule.class);

  private final Set<Module> modules = new HashSet<>();
  private Injector injector = null;

  @Override
  protected final void before() {
    this.modules.clear();
    this.installModule(new TestFrameworkModule());
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
    this.getInstance(TestClock.class).currentTimeDelta(unit.toMillis(delta) + shift);
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

}
