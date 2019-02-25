package tv.mechjack.mechjackbot.web;

import java.io.IOException;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.mechjackbot.api.ChatBotConfiguration;
import tv.mechjack.mechjackbot.web.tempdata.Application;
import tv.mechjack.mechjackbot.web.tempdata.WebappTypeAdapterRegistrar;
import tv.mechjack.platform.gson.TypeAdapterRegistrar;
import tv.mechjack.platform.web.ControllerHandler;
import tv.mechjack.platform.web.ErrorPageHandler;
import tv.mechjack.platform.web.ResourceBase;
import tv.mechjack.platform.web.ResourceBaseFactory;
import tv.mechjack.platform.web.WebApplication;
import tv.mechjack.platform.web.WebServerException;
import tv.mechjack.platform.web.services.Controller;
import tv.mechjack.platform.web.services.dispatch.ControllerServiceRegistry;
import tv.mechjack.platform.web.services.dispatch.ServiceCommandFactory;

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

    Multibinder.newSetBinder(this.binder(), Controller.class).addBinding()
        .to(ApplicationController.class).in(Scopes.SINGLETON);
    Multibinder.newSetBinder(this.binder(), Controller.class).addBinding()
        .to(TestController.class).in(Scopes.SINGLETON);
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

      // controllerHandler.registerController("/api/v1/application",
      //     ApplicationNotControllerServlet.class);
    }

    @Override
    public void registerErrorPages(final ErrorPageHandler errorPageHandler) {
      errorPageHandler.registerErrorPage(400, 599, "/api/v1/error");
    }

  }

}
