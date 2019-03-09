package tv.mechjack.platform.webapp.resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class InputStreamArgumentHandler
    implements ArgumentHandler {

  public boolean isHandler(final Class<?> argument,
      final Annotation[] annotations) {
    return argument.isAssignableFrom(InputStream.class);
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class argument, final Annotation[] annotations)
      throws IOException {
    return request.getInputStream();
  }

}
