package tv.mechjack.platform.webapp.services.dispatch;

import tv.mechjack.platform.webapp.services.BadRequestException;

@SuppressWarnings("serial")
public final class MethodNotAllowedException
    extends BadRequestException {

  public MethodNotAllowedException(final String message) {
    this(message, null);
  }

  public MethodNotAllowedException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
