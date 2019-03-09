package tv.mechjack.platform.webapp.resource;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import tv.mechjack.platform.webapp.api.resource.ArgumentHandler;

public final class DefaultArgumentHandlerRegistry implements
    ArgumentHandlerRegistry {

  private final List<ArgumentHandler> argumentHandlers;

  @Inject
  public DefaultArgumentHandlerRegistry(final Set<ArgumentHandler> argumentHandlers,
      final ObjectArgumentHandler objectArgumentHandler) {
    this.argumentHandlers = new ArrayList<>();
    this.argumentHandlers.addAll(argumentHandlers);
    this.argumentHandlers.add(objectArgumentHandler);
  }

  @Override
  public final Optional<ArgumentHandler> findArgumentHandler(final Class<?> argument,
      final Annotation[] annotations) {
    for (final ArgumentHandler argumentHandler : this.argumentHandlers) {
      if (argumentHandler.isHandler(argument, annotations)) {
        return Optional.of(argumentHandler);
      }
    }
    return Optional.empty();
  }

}