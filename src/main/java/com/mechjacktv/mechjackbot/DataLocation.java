package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class DataLocation extends TypedString {

  public static DataLocation of(final String value) {
    return TypedString.of(DataLocation.class, value);
  }

  private DataLocation(final String value) {
    super(value);
  }

}
