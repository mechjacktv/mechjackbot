package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.webapp.HttpStatusCode;

@SuppressWarnings("serial")
public final class ForbiddenException extends HttpServiceException {

  public ForbiddenException(final String message) {
    super(HttpStatusCode.FORBIDDEN, message);
  }

}
