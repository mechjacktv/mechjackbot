package tv.mechjack.util;

final class SoftenedException extends RuntimeException {

  private static final long serialVersionUID = -217972066245379725L;

  public SoftenedException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
