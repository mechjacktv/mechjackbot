package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class DataLocation extends TypedString {

  public static DataLocation of(final String value) {
    return new DataLocation(value);
  }

  private DataLocation(final String value) {
    super(value);
  }

}
