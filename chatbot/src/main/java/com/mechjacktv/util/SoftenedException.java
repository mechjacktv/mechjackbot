package com.mechjacktv.util;

final class SoftenedException extends RuntimeException {

    public SoftenedException(final Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public SoftenedException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
