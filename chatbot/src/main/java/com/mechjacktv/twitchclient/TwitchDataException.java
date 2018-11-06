package com.mechjacktv.twitchclient;

public final class TwitchDataException extends TwitchClientException {

  private static final long serialVersionUID = 1953550685625943603L;

  public TwitchDataException() {
    super(null, null);
  }

  public TwitchDataException(final String message) {
    super(message, null);
  }

  public TwitchDataException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public TwitchDataException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
