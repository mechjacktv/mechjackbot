package com.mechjacktv.twitchclient;

class TwitchClientException extends RuntimeException {

  TwitchClientException() {
    super(null, null);
  }

  TwitchClientException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
