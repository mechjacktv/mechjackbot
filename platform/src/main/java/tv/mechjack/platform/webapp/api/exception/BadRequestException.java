package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class BadRequestException extends HttpErrorStatusException {

  public BadRequestException(final String message) {
    this(message, null);
  }

  public BadRequestException(final String message, final Throwable cause) {
    super(HttpStatusCode.BAD_REQUEST, message, cause);
  }

}
