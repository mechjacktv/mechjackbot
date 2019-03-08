package tv.mechjack.platform.webapp.services.dispatch;

import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Strings;

import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.UriPattern;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandler.Reason;

public class DefaultRequestHandlerMatcher implements RequestHandlerMatcher {

  @Override
  public boolean isHandler(final HttpServletRequest request,
      final ContentType contentType, final HttpMethod httpMethod,
      final UriPattern uriPattern, final Consumer<Reason> observer) {
    if (!this.matchesRequestUri(request, uriPattern)) {
      observer.accept(Reason.URI_PATTERN);
      return false;
    } else if (!this.matchesHttpMethod(request, httpMethod)) {
      observer.accept(Reason.HTTP_METHOD);
      return false;
    } else if (!this.matchesContentType(request, contentType)) {
      observer.accept(Reason.CONTENT_TYPE);
      return false;
    }
    return true;
  }

  private boolean matchesRequestUri(final HttpServletRequest request,
      final UriPattern uriPattern) {
    final String requestUri = this.getRequestUriWithoutContextPath(request);
    final String regex = uriPattern.value.replaceAll(
        "(\\{\\{[\\w\\d_]+}})", "([\\\\w\\\\d_]+)");

    return requestUri.matches("(?i)" + regex);
  }

  private String getRequestUriWithoutContextPath(
      final HttpServletRequest request) {
    // TODO (2019-03-07 mechjack): more flexible context path concept
    final String requestUri = request.getRequestURI();
    final String contextPath = request.getContextPath();

    if (!Strings.isNullOrEmpty(contextPath)
        && requestUri.startsWith(contextPath)) {
      return requestUri.substring(contextPath.length());
    }
    return requestUri;
  }

  private boolean matchesHttpMethod(final HttpServletRequest request,
      final HttpMethod httpMethod) {
    return httpMethod.equals(this.getHttpMethod(request));
  }

  private HttpMethod getHttpMethod(final HttpServletRequest request) {
    final String requestMethod = Strings.nullToEmpty(request.getMethod()).toUpperCase();
    final String headerOverride = Strings.nullToEmpty(request.getHeader("X-HTTP-Method-Override"))
        .toUpperCase();
    final String paramOverride = Strings.nullToEmpty(request.getParameter("_method")).toUpperCase();

    switch (requestMethod) {
    case "GET":
      return HttpMethod.GET;
    case "POST":
      if ("PUT".equals(headerOverride)
          || "PUT".equals(paramOverride)) {
        return HttpMethod.PUT;
      } else if ("DELETE".equals(headerOverride)
          || "DELETE".equals(paramOverride)) {
        return HttpMethod.DELETE;
      }
      return HttpMethod.POST;
    case "PUT":
      return HttpMethod.PUT;
    case "DELETE":
      return HttpMethod.DELETE;
    }
    throw new MethodNotAllowedException(
        "method, " + request.getMethod() + ", not supported for resource");
  }

  private boolean matchesContentType(final HttpServletRequest request,
      final ContentType contentType) {
    if ("*/*".equalsIgnoreCase(contentType.value)) {
      return true;
    }
    return contentType.value.equalsIgnoreCase(request.getContentType());
  }
}
