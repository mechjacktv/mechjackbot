package tv.mechjack.mechjackbot;

import tv.mechjack.util.typedobject.TypedString;

public final class DataLocation extends TypedString {

  public static DataLocation of(final String value) {
    return TypedString.of(DataLocation.class, value);
  }

  private DataLocation(final String value) {
    super(value);
  }

}
