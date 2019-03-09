package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class ConflictException extends HttpErrorStatusException {

  public ConflictException(final String message) {
    super(HttpStatusCode.CONFLICT, message);
  }

}
