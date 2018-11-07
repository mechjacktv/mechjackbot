package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class DataLocation extends TypedString {

  private DataLocation(final String value) {
    super(value);
  }

  public static DataLocation of(final String value) {
    return new DataLocation(value);
  }

}
