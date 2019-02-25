package tv.mechjack.platform.web.services.dispatch;

import java.io.IOException;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public final class ControllerFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ControllerFilter.class);

  private final ControllerServiceRegistry controllerServiceRegistry;

  @Inject
  public ControllerFilter(
      final ControllerServiceRegistry controllerServiceRegistry) {
    this.controllerServiceRegistry = controllerServiceRegistry;
  }

  @Override
  public void init(final FilterConfig filterConfig) throws ServletException {
    /* no-op (2019-02-24 mechjack) */
  }

  @Override
  public void doFilter(final ServletRequest request,
      final ServletResponse response,
      final FilterChain chain) throws IOException, ServletException {
    LOGGER.info("Checking request for handler");
    if (this.isNotHandled(request, response)) {
      LOGGER.info("Request is not handled");
      chain.doFilter(request, response);
    }
  }

  private boolean isNotHandled(final ServletRequest request,
      final ServletResponse response)
      throws IOException {
    return !this.controllerServiceRegistry.handleRequest(
        (HttpServletRequest) request,
        (HttpServletResponse) response);
  }

  @Override
  public void destroy() {
    /* no-op (2019-02-24 mechjack) */
  }

}
