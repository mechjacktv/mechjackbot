package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.webapp.HttpStatusCode;

@SuppressWarnings("serial")
public final class InternalServerErrorException extends HttpServiceException {

  public InternalServerErrorException(final String message) {
    this(message, null);
  }

  public InternalServerErrorException(final String message, final Throwable cause) {
    super(HttpStatusCode.INTERNAL_SERVER_ERROR, message, cause);
  }

}
