package tv.mechjack.platform.webapp.resource;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.multibindings.OptionalBinder;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.ExceptionHandler;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class ResourceDispatchModule extends AbstractModule {

  @Override
  protected final void configure() {
    this.configureArgumentHandlers();
    this.configureResponseHandlers();

    OptionalBinder.newOptionalBinder(this.binder(), ExceptionHandler.class)
        .setDefault().to(DefaultExceptionHandler.class)
        .in(Scopes.SINGLETON);

    this.bind(ResourceCommandComparator.class)
        .to(DefaultResourceCommandComparator.class)
        .in(Scopes.SINGLETON);

    this.bind(ResourceCommandFactory.class)
        .to(DefaultResourceCommandFactory.class)
        .in(Scopes.SINGLETON);

    this.bind(ResouceCommandMatcher.class)
        .to(DefaultResouceCommandMatcher.class)
        .in(Scopes.SINGLETON);

    this.bind(ResourceCommandRegistry.class)
        .to(DefaultResourceCommandRegistry.class)
        .in(Scopes.SINGLETON);

    this.bind(ResourceDispatchFilter.class)
        .in(Scopes.SINGLETON);
  }

  private void configureArgumentHandlers() {
    this.bind(ArgumentHandlerRegistry.class)
        .to(DefaultArgumentHandlerRegistry.class)
        .in(Scopes.SINGLETON);

    this.bind(ObjectArgumentHandler.class)
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
        .addBinding().to(UriPatternGroupArgumentHandler.class)
        .in(Scopes.SINGLETON);
  }

  private void configureResponseHandlers() {
    this.bind(ResponseHandlerRegistry.class)
        .to(DefaultResponseHandlerRegistry.class)
        .in(Scopes.SINGLETON);

    this.bind(NullResponseHandler.class)
        .in(Scopes.SINGLETON);

    this.bind(ObjectResponseHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ResponseHandler.class)
        .addBinding().to(ByteArrayResponseHandler.class)
        .in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), ResponseHandler.class)
        .addBinding().to(PrimitiveResponseHandler.class)
        .in(Scopes.SINGLETON);
  }

}
