package tv.mechjack.platform.webapp.resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.Objects;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tv.mechjack.platform.webapp.api.exception.UnsupportedMediaTypeException;
import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class ObjectArgumentHandler
    implements ArgumentHandler {

  private static final int BUFFER_SIZE = 4 * 1024;

  private final Gson gson;

  @Inject
  public ObjectArgumentHandler(final Gson gson) {
    this.gson = gson;
  }

  public boolean isHandler(final Class<?> argument,
      final Annotation[] annotations) {
    return true;
  }

  public Object handle(final HttpServletRequest request,
      final HttpServletResponse response, final UriPattern uriPattern,
      final Class<?> argument, final Annotation[] annotations)
      throws IOException {
    if (Objects.nonNull(request.getContentType())) {
      if (request.getContentType().startsWith("text/plain") && argument
          .isAssignableFrom(String.class)) {
        return this.getRequestBody(request);
      } else if (request.getContentType().startsWith("application/json")) {
        return this.getRequestResource(request, argument);
      }
      throw new UnsupportedMediaTypeException(request.getContentType());
    }
    return null;
  }

  private String getRequestBody(final HttpServletRequest request)
      throws IOException {
    final StringWriter out = new StringWriter();
    final BufferedReader in = request.getReader();
    final char[] buf = new char[BUFFER_SIZE];
    int len;

    while ((len = in.read(buf, 0, buf.length)) != -1) {
      out.write(buf, 0, len);
    }
    return out.toString();
  }

  private Object getRequestResource(final HttpServletRequest request,
      final Class<?> resourceType)
      throws IOException {
    return this.gson.fromJson(this.getRequestBody(request), resourceType);
  }

}
