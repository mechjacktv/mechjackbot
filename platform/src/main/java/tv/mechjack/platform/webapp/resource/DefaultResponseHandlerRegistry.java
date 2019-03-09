package tv.mechjack.platform.webapp.resource;

import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

public final class DefaultResponseHandlerRegistry
    implements ResponseHandlerRegistry {

  private final Set<ResponseHandler> responseHandlers;
  private final NullResponseHandler nullResponseHandler;
  private final ObjectResponseHandler objectResponseHandler;

  @Inject
  public DefaultResponseHandlerRegistry(
      final Set<ResponseHandler> responseHandlers,
      final NullResponseHandler nullResponseHandler,
      final ObjectResponseHandler objectResponseHandler) {
    this.responseHandlers = responseHandlers;
    this.nullResponseHandler = nullResponseHandler;
    this.objectResponseHandler = objectResponseHandler;
  }

  @Override
  public final Optional<ResponseHandler> findResponseHandler(
      final HttpServletRequest request,
      final Object resource) {
    if (this.nullResponseHandler.isHandler(request, resource)) {
      return Optional.of(this.nullResponseHandler);
    }
    for (final ResponseHandler responseHandler : this.responseHandlers) {
      if (responseHandler.isHandler(request, resource)) {
        return Optional.of(responseHandler);
      }
    }
    return Optional.of(this.objectResponseHandler);
  }

}
