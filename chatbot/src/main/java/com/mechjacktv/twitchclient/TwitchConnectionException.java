package com.mechjacktv.twitchclient;

public final class TwitchConnectionException extends TwitchClientException {

  private static final long serialVersionUID = -6104859485634065957L;

  public TwitchConnectionException() {
    super(null, null);
  }

  public TwitchConnectionException(final String message) {
    super(message, null);
  }

  public TwitchConnectionException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public TwitchConnectionException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
