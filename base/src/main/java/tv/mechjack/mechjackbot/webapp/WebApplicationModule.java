package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import tv.mechjack.platform.webserver.ResourceBase;
import tv.mechjack.platform.webserver.ResourceBaseFactory;
import tv.mechjack.platform.webserver.WebApplication;
import tv.mechjack.platform.webserver.WebServerException;

public class WebApplicationModule extends AbstractModule {

  @Override
  protected void configure() {
    Multibinder.newSetBinder(this.binder(), WebApplication.class).addBinding()
        .to(BaseWebApplication.class).in(Scopes.SINGLETON);
  }

  private static class BaseWebApplication implements WebApplication {

    private final ResourceBase resourceBase;

    @Inject
    public BaseWebApplication(final ResourceBaseFactory resourceBaseFactory) {
      try {
        this.resourceBase = resourceBaseFactory
            .createResourceBase(WebApplicationModule.class);
      } catch (IOException e) {
        throw new WebServerException(e.getMessage(), e);
      }
    }

    @Override
    public String getContextPath() {
      return "/";
    }

    @Override
    public String getResourceBase() {
      return this.resourceBase.getPath();
    }

  }

}
