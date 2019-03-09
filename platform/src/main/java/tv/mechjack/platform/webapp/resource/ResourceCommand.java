package tv.mechjack.platform.webapp.resource;

import java.io.IOException;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ResourceCommand extends Comparable<ResourceCommand> {

  enum Reason
  {
    URI_PATTERN,
    HTTP_METHOD,
    CONTENT_TYPE
  }

  boolean isHandler(HttpServletRequest request,
      Consumer<Reason> observer);

  Object handle(HttpServletRequest request,
      HttpServletResponse response) throws IOException;

}
