package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class UnsupportedMediaTypeException
    extends HttpErrorStatusException {

  public UnsupportedMediaTypeException(final String message) {
    super(HttpStatusCode.UNSUPPORTED_MEDIA_TYPE, message);
  }

}
