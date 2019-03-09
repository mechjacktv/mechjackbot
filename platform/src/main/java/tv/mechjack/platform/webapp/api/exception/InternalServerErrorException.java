package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class InternalServerErrorException extends
    HttpErrorStatusException {

  public InternalServerErrorException(final String message) {
    this(message, null);
  }

  public InternalServerErrorException(final String message, final Throwable cause) {
    super(HttpStatusCode.INTERNAL_SERVER_ERROR, message, cause);
  }

}
