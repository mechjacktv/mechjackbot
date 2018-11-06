package com.mechjacktv.util;

public class MessageParsingException extends RuntimeException {

    public MessageParsingException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public MessageParsingException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
