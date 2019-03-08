package tv.mechjack.platform.webapp.services.dispatch;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tv.mechjack.platform.webapp.HttpStatusCode;
import tv.mechjack.platform.webapp.services.ArgumentHandler;
import tv.mechjack.platform.webapp.services.Controller;
import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.HttpServiceException;
import tv.mechjack.platform.webapp.services.InternalServerErrorException;
import tv.mechjack.platform.webapp.services.UriPattern;

public final class DefaultRequestHandler implements RequestHandler {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(DefaultRequestHandler.class);

  private final ArgumentHandlerRegistry argumentHandlerRegistry;
  private final Gson gson;
  private final RequestHandlerMatcher requestHandlerMatcher;
  private final Controller controller;
  private final Method handler;
  private final ContentType contentType;
  private final HttpMethod httpMethod;
  private final UriPattern uriPattern;

  DefaultRequestHandler(final ArgumentHandlerRegistry argumentHandlerRegistry,
      final Gson gson, final RequestHandlerMatcher requestHandlerMatcher,
      final Controller controller, final Method handler,
      final ContentType contentType, final HttpMethod httpMethod,
      final UriPattern uriPattern) {
    this.argumentHandlerRegistry = argumentHandlerRegistry;
    this.gson = gson;
    this.requestHandlerMatcher = requestHandlerMatcher;
    this.controller = controller;
    this.handler = handler;
    this.contentType = contentType;
    this.httpMethod = httpMethod;
    this.uriPattern = uriPattern;
  }

  @Override
  public int compareTo(final RequestHandler other) {
    if (other instanceof DefaultRequestHandler) {
      final DefaultRequestHandler defaultOther = (DefaultRequestHandler) other;
      final int thisUriPatternDepth = this.uriPattern.value.split("/").length;
      final int otherUriPatterDepth = defaultOther.uriPattern.value
          .split("/").length;
      final int thisUriPatternLength = this.uriPattern.value.length();
      final int otherUriPatternLength = defaultOther.uriPattern.value.length();
      final int thisContentTypeLength = this.contentType.value.length();
      final int otherContentTypeLength = defaultOther.contentType.value
          .length();

      if (thisUriPatternDepth != otherUriPatterDepth) {
        return -(thisUriPatternDepth - otherUriPatterDepth);
      } else if (thisUriPatternLength != otherUriPatternLength) {
        return thisUriPatternLength - otherUriPatternLength;
      } else if (thisContentTypeLength != otherContentTypeLength) {
        return -(thisContentTypeLength - otherContentTypeLength);
      }
    }
    return 0;
  }

  @Override
  public final boolean isHandler(final HttpServletRequest request,
      Consumer<Reason> observer) {
    return this.requestHandlerMatcher.isHandler(request, this.contentType,
        this.httpMethod, this.uriPattern, observer);
  }

  @Override
  public final void handle(final HttpServletRequest request,
      final HttpServletResponse response) throws IOException {
    this.renderResponse(this.invokeAction(request, response), request,
        response);
  }

  private Object invokeAction(final HttpServletRequest request,
      final HttpServletResponse response)
      throws IOException {
    final Object[] arguments = this.getArguments(request, response);

    try {
      return this.handler.invoke(this.controller, arguments);
    } catch (final IllegalArgumentException | IllegalAccessException e) {
      throw new InternalServerErrorException(e.getMessage(), e);
    } catch (InvocationTargetException e) {
      if (e.getCause() instanceof HttpServiceException) {
        throw (HttpServiceException) e.getCause();
      } else if (e.getCause() instanceof InternalServerErrorException) {
        throw (InternalServerErrorException) e.getCause();
      }
      throw new InternalServerErrorException(e.getMessage(), e);
    }
  }

  private Object[] getArguments(final HttpServletRequest request,
      final HttpServletResponse response)
      throws IOException {
    final Class[] argumentTypes = this.handler.getParameterTypes();
    final Annotation[][] argumentAnnotations = this.handler
        .getParameterAnnotations();
    final Object[] arguments = new Object[argumentTypes.length];

    for (int i = 0; i < argumentTypes.length; i++) {
      final Optional<ArgumentHandler> argumentHandler = this.argumentHandlerRegistry.findArgumentHandler(
          argumentTypes[i],
          argumentAnnotations[i]);

      if (argumentHandler.isPresent()) {
        arguments[i] = argumentHandler.get().handle(request, response,
            this.uriPattern, argumentTypes[i], argumentAnnotations[i]);
      }
    }
    return arguments;
  }

  private void renderResponse(final Object resource,
      final HttpServletRequest request,
      final HttpServletResponse response)
      throws IOException {
    response.setStatus(HttpStatusCode.OK.toInteger());
    if (resource != null) {
      if (resource instanceof String) {
        response.setContentType("text/plain; charset="
            + Charset.defaultCharset());
        response.getWriter().print(resource);
      } else {
        response.setContentType("application/json; charset="
            + Charset.defaultCharset());
        response.getWriter().print(this.gson.toJson(resource));
      }
    }
  }

}
