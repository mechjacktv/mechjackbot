package com.mechjacktv.twitchclient;

public class TwitchClientConnectException extends TwitchClientException {

    public TwitchClientConnectException() {
        super(null, null);
    }

    public TwitchClientConnectException(final String message) {
        super(message, null);
    }

    public TwitchClientConnectException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public TwitchClientConnectException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
