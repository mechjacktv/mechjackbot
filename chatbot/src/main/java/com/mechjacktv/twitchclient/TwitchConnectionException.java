package com.mechjacktv.twitchclient;

public final class TwitchConnectionException extends TwitchClientException {

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
