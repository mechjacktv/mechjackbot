package tv.mechjack.util;

public final class MessageParsingException extends RuntimeException {

  private static final long serialVersionUID = -2368341402621573828L;

  public MessageParsingException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
