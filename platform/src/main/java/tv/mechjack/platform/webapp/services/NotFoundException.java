package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.webapp.HttpStatusCode;

@SuppressWarnings("serial")
public final class NotFoundException extends HttpServiceException {

  public NotFoundException(final String message) {
    super(HttpStatusCode.NOT_FOUND, message);
  }

}
