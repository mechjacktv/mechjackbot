package com.mechjacktv.twitchclient;

public class TwitchDataException extends TwitchClientException {

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
