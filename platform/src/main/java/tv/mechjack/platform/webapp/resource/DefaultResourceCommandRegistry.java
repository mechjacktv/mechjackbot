package tv.mechjack.platform.webapp.resource;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.api.ContentType;
import tv.mechjack.platform.webapp.api.exception.MethodNotAllowedException;
import tv.mechjack.platform.webapp.api.exception.UnsupportedMediaTypeException;
import tv.mechjack.platform.webapp.api.resource.Controller;
import tv.mechjack.platform.webapp.api.resource.RequestHandler;
import tv.mechjack.platform.webapp.api.resource.UriPattern;
import tv.mechjack.platform.webapp.resource.ResourceCommand.Reason;

public final class DefaultResourceCommandRegistry implements
    ResourceCommandRegistry {

  private final Set<ResourceCommand> resourceCommands;
  private final ResourceCommandFactory resourceCommandFactory;

  @Inject
  public DefaultResourceCommandRegistry(
      final ResourceCommandFactory resourceCommandFactory,
      final Set<Controller> controllers) {
    this.resourceCommands = new TreeSet<>();
    this.resourceCommandFactory = resourceCommandFactory;
    this.loadRequestHandlers(controllers);
  }

  private void loadRequestHandlers(final Set<Controller> controllers) {
    for (final Controller controller : controllers) {
      for (final Method handler : controller.getClass().getMethods()) {
        final RequestHandler requestHandler = handler
            .getAnnotation(RequestHandler.class);

        if (Objects.nonNull(requestHandler)) {
          this.resourceCommands.add(this.resourceCommandFactory
              .createRequestHandler(controller, handler,
                  ContentType.of(requestHandler.contentType()),
                  requestHandler.method(),
                  UriPattern.of(controller.getRootUriPattern()
                      + requestHandler.uriPattern())));
        }
      }
    }
  }

  @Override
  public Optional<ResourceCommand> findResourceCommand(
      final HttpServletRequest request) {
    final AtomicBoolean rejectedMethod = new AtomicBoolean(false);
    final AtomicBoolean rejectedContentType = new AtomicBoolean(false);

    for (final ResourceCommand resourceCommand : this.resourceCommands) {
      if (resourceCommand.isHandler(request, reason -> {
        if (Reason.HTTP_METHOD.equals(reason)) {
          rejectedMethod.set(true);
        } else if (Reason.CONTENT_TYPE.equals(reason)) {
          rejectedContentType.set(true);
        }
      })) {
        return Optional.of(resourceCommand);
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
