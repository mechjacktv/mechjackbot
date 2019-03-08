package tv.mechjack.platform.webapp.services.dispatch.argumenthandlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tv.mechjack.platform.webapp.services.ArgumentHandler;
import tv.mechjack.platform.webapp.services.UriMatch;
import tv.mechjack.platform.webapp.services.UriPattern;

public final class UriMatchArgumentHandler implements ArgumentHandler {

  public boolean isHandler(final Class<?> argument,
      final Annotation[] annotations) {
    return Objects.nonNull(this.getUriMatch(annotations));
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations) {
    final String uriMatchName = Objects.requireNonNull(
        this.getUriMatch(annotations)).value();

    if (argument.isAssignableFrom(String.class)) {
      return this.getParameters(request, uriPattern).get(uriMatchName);
    }
    return null;
  }

  private UriMatch getUriMatch(final Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof UriMatch) {
        return (UriMatch) annotation;
      }
    }
    return null;
  }

  private Map<String, Object> getParameters(final HttpServletRequest request,
      final UriPattern uriPattern) {
    final Map<String, Object> parameters = new HashMap<>();

    parameters.putAll(this.getRegexPathParameters(request, uriPattern));
    parameters.putAll(this.getNamedPathParameters(request, uriPattern));
    return parameters;
  }

  private Map<String, Object> getRegexPathParameters(
      final HttpServletRequest request, final UriPattern uriPattern) {
    final Map<String, Object> parameters = new HashMap<>();
    final String serviceHandlerPath = uriPattern.value
        .replaceAll("(\\{\\{[\\w\\d_]+}})", "([\\\\w\\\\d_]+)");
    final String regex = request.getContextPath() + serviceHandlerPath;
    final Matcher matcher = Pattern.compile(regex)
        .matcher(request.getRequestURI());

    if (matcher.matches()) {
      parameters.put("$0", matcher.group(0));
      for (int i = 0; i < matcher.groupCount(); i++) {
        parameters.put("$" + (i + 1), matcher.group(i + 1));
      }
    }
    return parameters;
  }

  private Map<String, Object> getNamedPathParameters(
      final HttpServletRequest request, final UriPattern uriPattern) {
    final Map<String, Object> parameters = new HashMap<>();
    final String serviceHandlerPath = uriPattern.value
        .replaceAll("(\\{\\{[\\w\\d_]+}})", "([\\\\w\\\\d_]+)");
    final String regex = request.getContextPath() + serviceHandlerPath;
    final List<String> keys = this.getPathParameterKeys(uriPattern);
    final Matcher matcher = Pattern.compile(regex)
        .matcher(request.getRequestURI());

    if (matcher.matches()) {
      for (int i = 0; i < keys.size(); i++) {
        parameters.put(keys.get(i), matcher.group(i + 1));
      }
    }
    return parameters;
  }

  private List<String> getPathParameterKeys(final UriPattern uriPattern) {
    final List<String> parameterNames = new ArrayList<>();
    final Matcher parameterNameMatcher = Pattern
        .compile("\\{\\{([\\w\\d_]+)}}").matcher(uriPattern.value);

    while (parameterNameMatcher.find()) {
      parameterNames.add(parameterNameMatcher.group(1));
    }
    return parameterNames;
  }

}
