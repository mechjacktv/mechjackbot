package com.mechjacktv.twitchclient;

public class TwitchClientException extends RuntimeException {

    public TwitchClientException() {
        super(null, null);
    }

    public TwitchClientException(final String message) {
        super(message, null);
    }

    public TwitchClientException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public TwitchClientException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
