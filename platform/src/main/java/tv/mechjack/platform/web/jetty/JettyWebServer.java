package tv.mechjack.platform.web.jetty;

import org.eclipse.jetty.server.Server;

import tv.mechjack.platform.web.WebServer;
import tv.mechjack.platform.web.WebServerException;

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
