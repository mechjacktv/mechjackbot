package tv.mechjack.platform.webapp.api.exception;

import tv.mechjack.platform.webapp.api.HttpStatusCode;

public final class UnauthorizedException extends HttpErrorStatusException {

  public UnauthorizedException(final String message) {
    this(message, "Basic realm=\"Application\"");
  }

  public UnauthorizedException(final String message,
      final String authorizationHeader) {
    super(HttpStatusCode.UNAUTHORIZED, message);
    this.setHeader("WWW-Authenticate", authorizationHeader);
  }

}
