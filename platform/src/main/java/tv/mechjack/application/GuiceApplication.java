package tv.mechjack.application;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public abstract class GuiceApplication extends BaseApplication implements Module {

  private final Injector injector;

  protected GuiceApplication(final String defaultApplicationDataLocation, final Module module) {
    this(defaultApplicationDataLocation, Sets.newHashSet(module));
  }

  protected GuiceApplication(final String defaultApplicationDataLocation, final Collection<Module> modules) {
    super(defaultApplicationDataLocation);
    final Set<Module> allModules = new HashSet<>(modules);

    allModules.add(this);
    allModules.add(new GuiceModule());
    this.injector = Guice.createInjector(allModules);
    DefaultInjectorBridge.INSTANCE.setInjector(this.injector);
  }

  public final void start() {
    this.start(this.injector);
  }

  protected abstract void start(Injector injector);

  @Override
  public void configure(final Binder binder) {
    /* no-op (2019-01-06 mechjack) */
  }

  @Provides
  @Singleton
  public final Application getApplication() {
    return this;
  }

}
