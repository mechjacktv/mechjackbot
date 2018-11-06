package com.mechjacktv.twitchclient;

class TwitchClientException extends RuntimeException {

  private static final long serialVersionUID = -4386263575015040004L;

  TwitchClientException() {
    super(null, null);
  }

  TwitchClientException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
