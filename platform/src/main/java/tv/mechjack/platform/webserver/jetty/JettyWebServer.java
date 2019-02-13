package tv.mechjack.platform.webserver.jetty;

import org.eclipse.jetty.server.Server;

import tv.mechjack.platform.webserver.WebServer;
import tv.mechjack.platform.webserver.WebServerException;

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
