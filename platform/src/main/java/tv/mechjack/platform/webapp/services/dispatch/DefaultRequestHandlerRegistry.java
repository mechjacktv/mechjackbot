package tv.mechjack.platform.webapp.services.dispatch;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.services.Action;
import tv.mechjack.platform.webapp.services.Controller;
import tv.mechjack.platform.webapp.services.HttpMethod;
import tv.mechjack.platform.webapp.services.Service;
import tv.mechjack.platform.webapp.services.UriPattern;
import tv.mechjack.platform.webapp.services.dispatch.RequestHandler.Reason;

public final class DefaultRequestHandlerRegistry implements
    RequestHandlerRegistry {

  private final Set<RequestHandler> requestHandlers;
  private final RequestHandlerFactory requestHandlerFactory;

  @Inject
  public DefaultRequestHandlerRegistry(
      final RequestHandlerFactory requestHandlerFactory,
      final Set<Controller> controllers) {
    this.requestHandlers = new TreeSet<>();
    this.requestHandlerFactory = requestHandlerFactory;
    this.loadRequestHandlers(controllers);
  }

  private void loadRequestHandlers(final Set<Controller> controllers) {
    for (final Controller controller : controllers) {
      for (final Method handler : controller.getClass().getMethods()) {
        final Action actionAnnotation = handler.getAnnotation(Action.class);
        final Service serviceAnnotation = handler
            .getAnnotation(Service.class);

        if (Objects.nonNull(actionAnnotation)) {
          this.requestHandlers.add(this.requestHandlerFactory
              .createRequestHandler(controller, handler,
                  ContentType.of(actionAnnotation.contentType()),
                  this.getHttpMethodForType(actionAnnotation.type()),
                  this.getUriPattern(controller.getUriRootPattern(),
                      actionAnnotation.type())));
        } else if (Objects.nonNull(serviceAnnotation)) {
          this.requestHandlers.add(this.requestHandlerFactory
              .createRequestHandler(controller, handler,
                  ContentType.of(serviceAnnotation.contentType()),
                  serviceAnnotation.method(),
                  UriPattern.of(controller.getUriRootPattern()
                      + serviceAnnotation.uriPattern())));
        }
      }
    }
  }

  private HttpMethod getHttpMethodForType(final Action.Type type) {
    switch (type) {
    case CREATE:
      return HttpMethod.POST;
    case UPDATE:
      return HttpMethod.PUT;
    case DESTROY:
      return HttpMethod.DELETE;
    default:
      return HttpMethod.GET;
    }
  }

  private UriPattern getUriPattern(final UriPattern rootUriPattern,
      final Action.Type type) {
    switch (type) {
    case SHOW:
    case UPDATE:
    case DESTROY:
      return UriPattern.of(rootUriPattern + "/{{id}}");
    default:
      return rootUriPattern;
    }
  }

  @Override
  public Optional<RequestHandler> findRequestHandler(
      final HttpServletRequest request) {
    final AtomicBoolean rejectedMethod = new AtomicBoolean(false);
    final AtomicBoolean rejectedContentType = new AtomicBoolean(false);

    for (final RequestHandler requestHandler : this.requestHandlers) {
      if (requestHandler.isHandler(request, reason -> {
        if (Reason.HTTP_METHOD.equals(reason)) {
          rejectedMethod.set(true);
        } else if (Reason.CONTENT_TYPE.equals(reason)) {
          rejectedContentType.set(true);
        }
      })) {
        return Optional.of(requestHandler);
      }
    }
    if (rejectedMethod.get()) {
      throw new MethodNotAllowedException("method, " + request.getMethod()
          + ", not supported for resource");
    } else if (rejectedContentType.get()) {
      throw new UnsupportedMediaTypeException(request.getContentType());
    }
    return Optional.empty();
  }

}
