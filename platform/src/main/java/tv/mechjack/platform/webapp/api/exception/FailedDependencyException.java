package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class FailedDependencyException extends HttpErrorStatusException {

  public FailedDependencyException(final String message) {
    this(message, null);
  }

  public FailedDependencyException(final String message, final Throwable cause) {
    super(HttpStatusCode.FAILED_DEPENDENCY, message, cause);
  }

}
