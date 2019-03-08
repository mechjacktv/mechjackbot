package tv.mechjack.platform.webapp;

public class WebServerException extends RuntimeException {

  public WebServerException(final String message) {
    super(message);
  }

  public WebServerException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
