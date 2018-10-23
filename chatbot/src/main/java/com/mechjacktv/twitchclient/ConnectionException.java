package com.mechjacktv.twitchclient;

public class ConnectionException extends TwitchClientException {

    public ConnectionException() {
        super(null, null);
    }

    public ConnectionException(final String message) {
        super(message, null);
    }

    public ConnectionException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public ConnectionException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
