package tv.mechjack.platform.webapp.services.dispatch.argumenthandlers;

import java.io.IOException;
import java.io.Reader;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.services.ArgumentHandler;
import tv.mechjack.platform.webapp.services.UriPattern;

public final class ReaderArgumentHandler implements ArgumentHandler {

  public boolean isHandler(final Class<?> argument,
      final Annotation[] annotations) {
    return argument.isAssignableFrom(Reader.class);
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations)
      throws IOException {
    return request.getReader();
  }

}
