package tv.mechjack.platform.webapp.services.dispatch.argumenthandlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.services.ArgumentHandler;
import tv.mechjack.platform.webapp.services.Header;
import tv.mechjack.platform.webapp.services.UriPattern;

public final class HeaderArgumentHandler implements ArgumentHandler {

  public boolean isHandler(final Class<?> argument,
      final Annotation[] annotations) {
    return Objects.nonNull(this.getHeader(annotations));
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations) {
    final String headerName = Objects.requireNonNull(
        this.getHeader(annotations)).value();

    if (argument.isAssignableFrom(String.class)) {
      return request.getHeader(headerName);
    } else if (argument.isAssignableFrom(List.class)) {
      final List<String> headerValues = new ArrayList<>();

      for (final Enumeration e = request.getHeaders(headerName); e.hasMoreElements();) {
        headerValues.add(e.nextElement().toString());
      }
      return headerValues;
    }
    return null;
  }

  private Header getHeader(final Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Header) {
        return (Header) annotation;
      }
    }
    return null;
  }

}
