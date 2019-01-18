package tv.mechjack.platform.application;

import tv.mechjack.platform.utils.typedobject.TypedString;

public final class ApplicationDataLocation extends TypedString {

  public static ApplicationDataLocation of(final String value) {
    return TypedString.of(ApplicationDataLocation.class, value);
  }

  private ApplicationDataLocation(final String value) {
    super(value);
  }

}
