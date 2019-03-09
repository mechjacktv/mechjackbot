package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class MethodNotAllowedException
    extends HttpErrorStatusException {

  public MethodNotAllowedException(final String message) {
    this(message, null);
  }

  public MethodNotAllowedException(final String message,
      final Throwable cause) {
    super(HttpStatusCode.METHOD_NOT_ALLOWED, message, cause);
  }

}
