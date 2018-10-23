package com.mechjacktv.twitchclient;

class TwitchClientException extends RuntimeException {

  TwitchClientException() {
    super(null, null);
  }

  public TwitchClientException(final String message) {
    super(message, null);
  }

  public TwitchClientException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

  TwitchClientException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
