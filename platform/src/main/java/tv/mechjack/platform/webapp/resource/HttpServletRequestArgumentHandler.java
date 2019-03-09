package tv.mechjack.platform.webapp.resource;

import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class HttpServletRequestArgumentHandler
    implements ArgumentHandler {

  public boolean isHandler(final Class argument,
      final Annotation[] annotations) {
    return argument.isAssignableFrom(HttpServletRequest.class);
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations) {
    return request;
  }

}
