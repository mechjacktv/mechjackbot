package tv.mechjack.platform.webapp.resource;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tv.mechjack.platform.webapp.api.exception.InternalServerErrorException;
import tv.mechjack.platform.webapp.api.resource.ExceptionHandler;
import tv.mechjack.platform.webapp.api.resource.ResponseHandler;

@Singleton
public final class ResourceDispatchFilter implements Filter {

  private final Gson gson;
  private final ExceptionHandler exceptionHandler;
  private final ResourceCommandRegistry resourceCommandRegistry;
  private final ResponseHandlerRegistry responseHandlerRegistry;

  @Inject
  public ResourceDispatchFilter(final Gson gson,
      final ExceptionHandler exceptionHandler,
      final ResourceCommandRegistry resourceCommandRegistry,
      final ResponseHandlerRegistry responseHandlerRegistry) {
    this.gson = gson;
    this.exceptionHandler = exceptionHandler;
    this.resourceCommandRegistry = resourceCommandRegistry;
    this.responseHandlerRegistry = responseHandlerRegistry;
  }

  @Override
  public final void init(final FilterConfig filterConfig) {
    /* no-op (2019-03-07 mechjack) */
  }

  public final void doFilter(final ServletRequest request,
      final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    if (this.isNotHandled(request, response)) {
      chain.doFilter(request, response);
    }
  }

  private boolean isNotHandled(final ServletRequest request,
      final ServletResponse response) throws IOException {
    return !this.handleRequest((HttpServletRequest) request,
        (HttpServletResponse) response);
  }

  private boolean handleRequest(final HttpServletRequest request,
      final HttpServletResponse response) throws IOException {
    try {
      final Optional<ResourceCommand> resourceCommand = this.resourceCommandRegistry.findResourceCommand(request);

      if (resourceCommand.isEmpty()) {
        return false;
      }
      this.renderResponse(request, response,
          resourceCommand.get().handle(request, response));
    } catch (final IOException e) {
      throw e;
    } catch (final Exception e) {
      this.exceptionHandler.handle(request, response, e);
    }
    return true;
  }

  private void renderResponse(final HttpServletRequest request,
      final HttpServletResponse response, final Object resource)
      throws IOException {
    Optional<ResponseHandler> responseHandler = this.responseHandlerRegistry.findResponseHandler(request, resource);

    if (responseHandler.isPresent()) {
      responseHandler.get().handle(request, response, resource);
    } else {
      throw new InternalServerErrorException(
          "Unable to process request response.");
    }
  }

  public final void destroy() {
    /* no-op (2019-03-07 mechjack) */
  }

}
