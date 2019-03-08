package tv.mechjack.platform.webapp.services.dispatch;

import tv.mechjack.platform.webapp.services.BadRequestException;

@SuppressWarnings("serial")
public final class UnsupportedMediaTypeException
    extends BadRequestException {

  public UnsupportedMediaTypeException(final String message) {
    super(message);
  }

}
