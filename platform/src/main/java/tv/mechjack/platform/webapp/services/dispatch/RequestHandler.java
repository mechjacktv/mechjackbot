package tv.mechjack.platform.webapp.services.dispatch;

import java.io.IOException;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RequestHandler extends Comparable<RequestHandler> {

  enum Reason
  {
    URI_PATTERN,
    HTTP_METHOD,
    CONTENT_TYPE
  }

  boolean isHandler(HttpServletRequest request,
      Consumer<Reason> observer);

  void handle(HttpServletRequest request,
      HttpServletResponse response) throws IOException;

}
