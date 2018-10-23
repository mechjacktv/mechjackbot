package com.mechjacktv.util;

final class SoftenedException extends RuntimeException {

  public SoftenedException() {
    super(null, null);
  }

  public SoftenedException(final String message) {
    super(message, null);
  }

  public SoftenedException(final Throwable cause) {
    super(cause.getMessage(), cause);
  }

  public SoftenedException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
