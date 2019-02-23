package tv.mechjack.platform.webserver.jetty;

import org.eclipse.jetty.servlet.ErrorPageErrorHandler;

import tv.mechjack.platform.webserver.ErrorPageHandler;

class DefaultErrorPageHandler implements ErrorPageHandler {

  private final ErrorPageErrorHandler errorHandler;

  DefaultErrorPageHandler(final ErrorPageErrorHandler errorHandler) {
    this.errorHandler = errorHandler;
  }

  @Override
  public void registerErrorPage(final int code, final String uri) {
    this.errorHandler.addErrorPage(code, uri);
  }

  @Override
  public void registerErrorPage(final int from, final int to,
      final String uri) {
    this.errorHandler.addErrorPage(from, to, uri);
  }

  @Override
  public void registerErrorPage(final Class<Throwable> throwableClass,
      final String uri) {
    this.errorHandler.addErrorPage(throwableClass, uri);
  }

}
