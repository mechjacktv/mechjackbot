package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class NotAcceptableException extends HttpErrorStatusException {

  public NotAcceptableException(final String message) {
    super(HttpStatusCode.NOT_ACCEPTABLE, message);
  }

}
