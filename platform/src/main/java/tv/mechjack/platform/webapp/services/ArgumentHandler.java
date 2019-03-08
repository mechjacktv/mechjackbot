package tv.mechjack.platform.webapp.services;

import java.io.IOException;
import java.lang.annotation.Annotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentHandler {

  boolean isHandler(final Class<?> argumentType, Annotation[] annotations);

  Object handle(HttpServletRequest request, HttpServletResponse response,
      UriPattern uriPattern, Class<?> argumentType, Annotation[] annotations)
      throws IOException;

}
