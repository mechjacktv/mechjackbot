package tv.mechjack.platform.webserver.jetty;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;

import tv.mechjack.platform.webserver.WebApplication;

public class ControllerContextListener extends GuiceServletContextListener {

  private final Injector rootInjector;
  private final WebApplication webApplication;

  public ControllerContextListener(final WebApplication webApplication,
      final Injector rootInjector) {
    this.rootInjector = rootInjector;
    this.webApplication = webApplication;
  }

  @Override
  protected Injector getInjector() {
    return this.rootInjector.createChildInjector(new ServletModule() {

      @Override
      protected void configureServlets() {
        ControllerContextListener.this.webApplication.registerControllers(
            (urlPattern, controller) -> {
              serve(urlPattern).with(controller);
            });
      }

    });
  }

}
