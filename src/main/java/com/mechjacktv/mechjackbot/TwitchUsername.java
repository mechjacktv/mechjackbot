package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchUsername extends TypedString {

  public static TwitchUsername of(final String value) {
    return TypedString.of(TwitchUsername.class, value);
  }

  private TwitchUsername(final String value) {
    super(value);
  }

}
