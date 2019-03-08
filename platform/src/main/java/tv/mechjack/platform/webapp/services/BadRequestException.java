package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.webapp.HttpStatusCode;

@SuppressWarnings("serial")
public class BadRequestException extends HttpServiceException {

  public BadRequestException(final String message) {
    this(message, null);
  }

  public BadRequestException(final String message, final Throwable cause) {
    super(HttpStatusCode.BAD_REQUEST, message, cause);
  }

}
