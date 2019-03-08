package tv.mechjack.platform.webapp.services;

import tv.mechjack.platform.webapp.HttpStatusCode;

@SuppressWarnings("serial")
public final class UnauthorizedException extends HttpServiceException {

  public UnauthorizedException(final String message) {
    this(message, "Basic realm=\"Application\"");
  }

  public UnauthorizedException(final String message,
      final String authorizationHeader) {
    super(HttpStatusCode.UNAUTHORIZED, message);
    this.setHeader("WWW-Authenticate", authorizationHeader);
  }

}
