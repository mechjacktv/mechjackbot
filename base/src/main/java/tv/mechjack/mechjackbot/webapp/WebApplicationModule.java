package tv.mechjack.mechjackbot.webapp;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.webapp.tempdata.Application;
import tv.mechjack.mechjackbot.webapp.tempdata.WebappTypeAdapterRegistrar;
import tv.mechjack.platform.gson.TypeAdapterRegistrar;
import tv.mechjack.platform.webserver.ControllerHandler;
import tv.mechjack.platform.webserver.ErrorPageHandler;
import tv.mechjack.platform.webserver.ResourceBase;
import tv.mechjack.platform.webserver.ResourceBaseFactory;
import tv.mechjack.platform.webserver.WebApplication;
import tv.mechjack.platform.webserver.WebServerException;

public class WebApplicationModule extends AbstractModule {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(WebApplicationModule.class);

  @Override
  protected void configure() {
    this.bind(Application.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), TypeAdapterRegistrar.class)
        .addBinding().to(WebappTypeAdapterRegistrar.class).in(Scopes.SINGLETON);

    Multibinder.newSetBinder(this.binder(), WebApplication.class).addBinding()
        .to(BaseWebApplication.class).in(Scopes.SINGLETON);
  }

  private static class BaseWebApplication implements WebApplication {

    private final ResourceBase resourceBase;
    private final ChatBotConfiguration chatBotConfiguration;

    @Inject
    public BaseWebApplication(final ResourceBaseFactory resourceBaseFactory,
        final ChatBotConfiguration chatBotConfiguration) {
      try {
        this.resourceBase = resourceBaseFactory
            .createResourceBase(WebApplicationModule.class);
        this.chatBotConfiguration = chatBotConfiguration;
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
      return System.getProperty(
          WebApplicationModule.class.getCanonicalName() + ".resource_base",
          this.resourceBase.getPath());
    }

    @Override
    public void registerControllers(final ControllerHandler controllerHandler) {
      controllerHandler.registerController("/api/v1/error",
          DefaultJsonErrorServlet.class);
      controllerHandler.registerController("/api/v1/wait",
          WaitServlet.class);

      controllerHandler.registerController("/api/v1/application",
          ApplicationControllerServlet.class);
    }

    @Override
    public void registerErrorPages(final ErrorPageHandler errorPageHandler) {
      errorPageHandler.registerErrorPage(400, 599, "/api/v1/error");
    }

  }

}
