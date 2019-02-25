package tv.mechjack.platform.web.jetty;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.web.WebApplication;
import tv.mechjack.platform.web.WebServer;
import tv.mechjack.platform.web.WebServerModule;
import tv.mechjack.platform.web.services.dispatch.ControllerFilter;
import tv.mechjack.platform.web.services.dispatch.ControllerServiceRegistry;
import tv.mechjack.platform.web.services.dispatch.ServiceCommandFactory;

public class JettyModule extends WebServerModule {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(JettyModule.class);

  public static final String DEFAULT_HTTP_HOST = "localhost";
  public static final ConfigurationKey KEY_HTTP_HOST = ConfigurationKey.of(
      "http_host", JettyModule.class);

  public static final String DEFAULT_HTTP_PORT = "8080";
  public static final ConfigurationKey KEY_HTTP_PORT = ConfigurationKey.of(
      "http_port", JettyModule.class);

  public static final String DEFAULT_HTTP_TIMEOUT = "30000";
  public static final ConfigurationKey KEY_HTTP_TIMEOUT = ConfigurationKey.of(
      "http_timeout", JettyModule.class);

  @Override
  protected void configure() {
    super.configure();
    this.bind(ControllerServiceRegistry.class).in(Scopes.SINGLETON);
    this.bind(ServiceCommandFactory.class).in(Scopes.SINGLETON);
    this.bind(ControllerFilter.class).in(Scopes.SINGLETON);
  }

  @Provides
  public final WebServer createJettyServer(final Configuration configuration,
      final ControllerFilter controllerFilter,
      final Set<WebApplication> webApplications, final Injector rootInjector) {
    final Server server = new Server();
    final ServerConnector connector = new ServerConnector(server);
    final ContextHandlerCollection handlers = new ContextHandlerCollection();

    connector.setHost(
        configuration.get(KEY_HTTP_HOST, DEFAULT_HTTP_HOST));
    connector.setPort(Integer.parseInt(
        configuration.get(KEY_HTTP_PORT, DEFAULT_HTTP_PORT)));
    connector.setIdleTimeout(Integer.parseInt(
        configuration.get(KEY_HTTP_TIMEOUT, DEFAULT_HTTP_TIMEOUT)));
    server.addConnector(connector);
    server.setRequestLog((req, res) -> {
      LOGGER.info("Context Path: " + req.getContextPath());
      LOGGER.info("Path Info: " + req.getPathInfo());
      LOGGER.info("Servlet Path: " + req.getServletPath());
      LOGGER.info(String.format("Request: %s %s", req, res));
    });
    server.setHandler(handlers);
    for (final WebApplication webApplication : webApplications) {
      handlers.addHandler(this.createHandler(controllerFilter, webApplication,
          rootInjector));
    }
    return new JettyWebServer(server);
  }

  private Handler createHandler(final ControllerFilter controllerFilter,
      final WebApplication webApplication,
      final Injector rootInjector) {
    final ServletContextHandler handler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    final FilterHolder filterHolder = new FilterHolder(controllerFilter);

    handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(
        DispatcherType.class));
    handler.addFilter(filterHolder, "/*", EnumSet.allOf(
        DispatcherType.class));
    handler.setContextPath(webApplication.getContextPath());
    handler.setResourceBase(webApplication.getResourceBase());
    handler.addServlet(DefaultServlet.class, "/");
    handler.getServletHandler()
        .addListener(this.createListenerHolder(webApplication, rootInjector));
    handler.setErrorHandler(this.createErrorHandler(webApplication));
    return handler;
  }

  private ListenerHolder createListenerHolder(
      final WebApplication webApplication, final Injector rootInjector) {
    final ListenerHolder listenerHolder = new ListenerHolder();

    listenerHolder.setListener(
        new ControllerContextListener(webApplication, rootInjector));
    return listenerHolder;
  }

  private ErrorHandler createErrorHandler(final WebApplication webApplication) {
    final ErrorPageErrorHandler errorHandler = new ErrorPageErrorHandler();

    webApplication.registerErrorPages(
        new DefaultErrorPageHandler(errorHandler));
    return errorHandler;
  }

}
