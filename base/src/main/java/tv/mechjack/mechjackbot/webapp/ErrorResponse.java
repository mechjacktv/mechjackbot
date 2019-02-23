package tv.mechjack.mechjackbot.webapp;

import javax.servlet.http.HttpServletRequest;

public final class ErrorResponse {

  private final String resource;
  private final String status;
  private final String message;

  public ErrorResponse(final HttpServletRequest req) {
    this(req.getAttribute("javax.servlet.error.request_uri").toString(),
        req.getAttribute("javax.servlet.error.status_code").toString(),
        req.getAttribute("javax.servlet.error.message").toString());
  }

  public ErrorResponse(final String resource, final String status,
      final String message) {
    this.resource = resource;
    this.status = status;
    this.message = message;
  }

  public String getResource() {
    return this.resource;
  }

  public String getStatus() {
    return this.status;
  }

  public String getMessage() {
    return this.message;
  }

}
