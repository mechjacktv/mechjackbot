package tv.mechjack.guice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
    final Set<Module> allModules = new HashSet<>(modules);

    allModules.add(new GuiceModule());
    this.injector = Guice.createInjector(allModules);
    DefaultInjectorBridge.INSTANCE.setInjector(this.injector);
  }

  public final void start() {
    this.start(this.injector);
  }

  protected abstract void start(Injector injector);

}
