package tv.mechjack.application;

import tv.mechjack.util.typedobject.TypedString;

public final class ApplicationDataLocation extends TypedString {

  public static ApplicationDataLocation of(final String value) {
    return TypedString.of(ApplicationDataLocation.class, value);
  }

  private ApplicationDataLocation(final String value) {
    super(value);
  }

}
