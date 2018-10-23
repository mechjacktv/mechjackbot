package com.mechjacktv.twitchclient;

public class InvalidResponseException extends TwitchClientException {

    public InvalidResponseException() {
        super(null, null);
    }

    public InvalidResponseException(final String message) {
        super(message, null);
    }

    public InvalidResponseException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidResponseException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
