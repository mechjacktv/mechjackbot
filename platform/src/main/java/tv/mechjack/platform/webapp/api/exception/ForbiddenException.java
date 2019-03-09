package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class ForbiddenException extends HttpErrorStatusException {

  public ForbiddenException(final String message) {
    super(HttpStatusCode.FORBIDDEN, message);
  }

}
