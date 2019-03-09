package tv.mechjack.platform.webapp.api.resource;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResponseHandler {

  boolean isHandler(HttpServletRequest request, Object resource);

  void handle(HttpServletRequest request, HttpServletResponse response,
      Object resource) throws IOException;

}
