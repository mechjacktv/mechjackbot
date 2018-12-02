package com.mechjacktv.testframework;

import java.util.HashSet;
import java.util.Set;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import org.junit.rules.ExternalResource;

public final class TestFrameworkRule extends ExternalResource {

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

  public final void installModule(final Module module) {
    this.modules.add(module);
  }

  public final <T> T getInstance(final Class<T> type) {
    if (this.injector == null) {
      this.injector = Guice.createInjector(this.modules);
    }
    return this.injector.getInstance(type);
  }

  public final String getArbitraryString() {
    return this.getInstance(ArbitraryDataGenerator.class).getString();
  }

}
