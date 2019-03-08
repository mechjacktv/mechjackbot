package tv.mechjack.platform.webapp.services.dispatch;

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

@Singleton
public final class DispatchFilter implements Filter {

  private final ExceptionHandler exceptionHandler;
  private final RequestHandlerRegistry requestHandlerRegistry;

  @Inject
  public DispatchFilter(final ExceptionHandler exceptionHandler,
      final RequestHandlerRegistry requestHandlerRegistry) {
    this.exceptionHandler = exceptionHandler;
    this.requestHandlerRegistry = requestHandlerRegistry;
  }

  @Override
  public void init(final FilterConfig filterConfig) {
    /* no-op (2019-03-07 mechjack) */
  }

  public void doFilter(final ServletRequest request,
      final ServletResponse response, final FilterChain chain)
      throws IOException, ServletException {
    if (this.isNotHandled(request, response)) {
      chain.doFilter(request, response);
    }
  }

  private boolean isNotHandled(final ServletRequest request,
      final ServletResponse response)
      throws IOException {
    return !this.handleRequest((HttpServletRequest) request,
        (HttpServletResponse) response);
  }

  private boolean handleRequest(final HttpServletRequest request,
      final HttpServletResponse response)
      throws IOException {
    try {
      final Optional<RequestHandler> serviceCommand = this.requestHandlerRegistry.findRequestHandler(request);

      if (serviceCommand.isEmpty()) {
        return false;
      }
      serviceCommand.get().handle(request, response);
    } catch (IOException e) {
      throw e;
    } catch (Exception e) {
      this.exceptionHandler.handle(request, response, e);
    }
    return true;
  }

  public void destroy() {
    /* no-op (2019-03-07 mechjack) */
  }

}
