package tv.mechjack.platform.webapp.resource;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.Parameter;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class ParameterArgumentHandler
    implements ArgumentHandler {

  public boolean isHandler(final Class argument,
      final Annotation[] annotations) {
    return Objects.nonNull(this.getParameter(annotations));
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations) {
    final String parameterName = Objects.requireNonNull(
        this.getParameter(annotations)).value();

    if (argument.isAssignableFrom(String.class)) {
      return request.getParameter(parameterName);
    } else if (argument.isAssignableFrom(List.class)) {
      final List<String> parameterValues = new ArrayList<>();

      if (Objects.nonNull(request.getParameterValues(parameterName))) {
        parameterValues.addAll(
            Arrays.asList(request.getParameterValues(parameterName)));
      }
      return parameterValues;
    }
    return null;
  }

  private Parameter getParameter(final Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof Parameter) {
        return (Parameter) annotation;
      }
    }
    return null;
  }

}
