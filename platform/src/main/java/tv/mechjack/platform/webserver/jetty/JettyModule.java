package tv.mechjack.platform.webserver.jetty;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;

import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ListenerHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.configuration.Configuration;
import tv.mechjack.platform.configuration.ConfigurationKey;
import tv.mechjack.platform.webserver.WebApplication;
import tv.mechjack.platform.webserver.WebServer;
import tv.mechjack.platform.webserver.WebServerModule;

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

  @Provides
  public final WebServer createJettyServer(final Configuration configuration,
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
      handlers.addHandler(createHandler(webApplication, rootInjector));
    }
    return new JettyWebServer(server);
  }

  private Handler createHandler(final WebApplication webApplication,
      final Injector rootInjector) {
    final ServletContextHandler handler = new ServletContextHandler(
        ServletContextHandler.SESSIONS);
    handler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(
        DispatcherType.class));
    handler.setContextPath(webApplication.getContextPath());
    handler.setResourceBase(webApplication.getResourceBase());
    handler.addServlet(DefaultServlet.class, "/");
    handler.getServletHandler()
        .addListener(createListenerHolder(webApplication, rootInjector));
    return handler;
  }

  private ListenerHolder createListenerHolder(
      final WebApplication webApplication, final Injector rootInjector) {
    final ListenerHolder listenerHolder = new ListenerHolder();

    listenerHolder.setListener(
        new ControllerContextListener(webApplication, rootInjector));
    return listenerHolder;
  }

}
