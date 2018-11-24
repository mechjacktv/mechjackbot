package com.mechjacktv.guice;

import java.util.Collection;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public abstract class GuiceApplication {

  private final Injector injector;

  public GuiceApplication(final Module module) {
    this(Sets.newHashSet(module));
  }

  protected GuiceApplication(final Collection<Module> modules) {
    this.injector = Guice.createInjector(modules);
    InjectorBridge.INSTANCE = new InjectorBridge(this.injector);
  }

  public final void start() {
    this.start(this.injector);
  }

  protected abstract void start(Injector injector);

}
