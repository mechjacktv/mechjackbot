package tv.mechjack.platform.webapp.services;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.platform.webapp.services.dispatch.ArgumentHandlerRegistry;
import tv.mechjack.platform.webapp.services.dispatch.DefaultArgumentHandlerRegistry;
import tv.mechjack.platform.webapp.services.dispatch.DefaultExceptionHandler;
import tv.mechjack.platform.webapp.services.dispatch.DefaultRequestHandlerFactory;
import tv.mechjack.platform.webapp.services.dispatch.DefaultRequestHandlerMatcher;
import tv.mechjack.platform.webapp.services.dispatch.DefaultRequestHandlerRegistry;
import tv.mechjack.platform.webapp.services.dispatch.DispatchFilter;
import tv.mechjack.platform.webapp.services.dispatch.ExceptionHandler;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandlerFactory;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandlerMatcher;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandlerRegistry;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.HeaderArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.HttpServletRequestArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.HttpServletResponseArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.InputStreamArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.ParameterArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.PojoArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.ReaderArgumentHandler;
import tv.mechjack.platform.webapp.services.dispatch.argumenthandlers.UriMatchArgumentHandler;

public class WebAppServicesModule extends AbstractModule {

  @Override
  protected void configure() {
    this.bind(ArgumentHandlerRegistry.class)
        .to(DefaultArgumentHandlerRegistry.class)
        .in(Scopes.SINGLETON);

    this.bind(DispatchFilter.class)
        .in(Scopes.SINGLETON);

    this.bind(ExceptionHandler.class)
        .to(DefaultExceptionHandler.class)
        .in(Scopes.SINGLETON);

    this.bind(RequestHandlerFactory.class)
        .to(DefaultRequestHandlerFactory.class)
        .in(Scopes.SINGLETON);

    this.bind(RequestHandlerMatcher.class)
        .to(DefaultRequestHandlerMatcher.class)
        .in(Scopes.SINGLETON);

    this.bind(RequestHandlerRegistry.class)
        .to(DefaultRequestHandlerRegistry.class)
        .in(Scopes.SINGLETON);

    this.bind(PojoArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(HeaderArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(HttpServletRequestArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(HttpServletResponseArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(InputStreamArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(ParameterArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(ReaderArgumentHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ArgumentHandler.class)
        .addBinding().to(UriMatchArgumentHandler.class)
        .in(Scopes.SINGLETON);
  }

}
