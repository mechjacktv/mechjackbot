package tv.mechjack.platform.web;

import javax.servlet.http.HttpServlet;

@FunctionalInterface
public interface ControllerHandler {

  void registerController(String urlPattern,
      Class<? extends HttpServlet> controller);

}
