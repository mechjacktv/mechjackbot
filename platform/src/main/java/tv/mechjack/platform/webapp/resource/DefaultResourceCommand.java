package tv.mechjack.platform.webapp.resource;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.HttpMethod;
import tv.mechjack.platform.webapp.api.exception.HttpErrorStatusException;
import tv.mechjack.platform.webapp.api.exception.InternalServerErrorException;
import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;
import tv.mechjack.platform.webapp.api.resource.Controller;
import tv.mechjack.platform.webapp.api.resource.UriPattern;

public final class DefaultResourceCommand implements ResourceCommand {

  private final ArgumentHandlerRegistry argumentHandlerRegistry;
  private final Gson gson;
  private final ResourceCommandComparator resourceCommandComparator;
  private final ResouceCommandMatcher resouceCommandMatcher;
  private final Controller controller;
  private final Method handler;
  private final ContentType contentType;
  private final HttpMethod httpMethod;
  private final UriPattern uriPattern;

  DefaultResourceCommand(final ArgumentHandlerRegistry argumentHandlerRegistry,
      final Gson gson, final ResourceCommandComparator resourceCommandComparator,
      final ResouceCommandMatcher resouceCommandMatcher,
      final Controller controller, final Method handler,
      final ContentType contentType, final HttpMethod httpMethod,
      final UriPattern uriPattern) {
    this.argumentHandlerRegistry = argumentHandlerRegistry;
    this.gson = gson;
    this.resourceCommandComparator = resourceCommandComparator;
    this.resouceCommandMatcher = resouceCommandMatcher;
    this.controller = controller;
    this.handler = handler;
    this.contentType = contentType;
    this.httpMethod = httpMethod;
    this.uriPattern = uriPattern;
  }

  @Override
  public final int compareTo(final ResourceCommand other) {
    if (other instanceof DefaultResourceCommand) {
      final DefaultResourceCommand defaultOther = (DefaultResourceCommand) other;
      final int uriPatternComparison = this.resourceCommandComparator
          .compare(this.uriPattern, defaultOther.uriPattern);

      if (uriPatternComparison != 0) {
        return uriPatternComparison;
      }

      final int contentTypeComparison = this.resourceCommandComparator
          .compare(this.contentType, defaultOther.contentType);

      if (contentTypeComparison != 0) {
        return contentTypeComparison;
      }
      return this.httpMethod.hashCode() - defaultOther.httpMethod.hashCode();
    }
    return this.hashCode() - other.hashCode();
  }

  @Override
  public final boolean isHandler(final HttpServletRequest request,
      Consumer<Reason> observer) {
    return this.resouceCommandMatcher.isHandler(request, this.contentType,
        this.httpMethod, this.uriPattern, observer);
  }

  @Override
  public final Object handle(final HttpServletRequest request,
      final HttpServletResponse response) throws IOException {
    return this.invokeAction(request, response);
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
      if (e.getCause() instanceof HttpErrorStatusException) {
        throw (HttpErrorStatusException) e.getCause();
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
      final Optional<ArgumentHandler> argumentHandler = this.argumentHandlerRegistry
          .findArgumentHandler(
              argumentTypes[i],
              argumentAnnotations[i]);

      if (argumentHandler.isPresent()) {
        arguments[i] = argumentHandler.get().handle(request, response,
            this.uriPattern, argumentTypes[i], argumentAnnotations[i]);
      }
    }
    return arguments;
  }

}
