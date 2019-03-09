package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class GoneException extends HttpErrorStatusException {

  public GoneException(final String message) {
    super(HttpStatusCode.GONE, message);
  }

}
