package tv.mechjack.platform.webapp.api.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ExceptionHandler {

  void handle(HttpServletRequest request, HttpServletResponse response, Exception e)
      throws IOException;

}
