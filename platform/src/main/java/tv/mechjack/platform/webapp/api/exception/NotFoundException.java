package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class NotFoundException extends HttpErrorStatusException {

  public NotFoundException(final String message) {
    super(HttpStatusCode.NOT_FOUND, message);
  }

}
