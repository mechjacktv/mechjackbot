package tv.mechjack.platform.webapp.jetty;

import org.eclipse.jetty.server.Server;

import tv.mechjack.platform.webapp.WebServer;
import tv.mechjack.platform.webapp.WebServerException;

class JettyWebServer implements WebServer {

  private final Server server;

  JettyWebServer(final Server server) {
    this.server = server;
  }

  @Override
  public void start() {
    try {
      this.server.start();
    } catch (Exception e) {
      throw new WebServerException(e.getMessage(), e);
    }
  }

}
