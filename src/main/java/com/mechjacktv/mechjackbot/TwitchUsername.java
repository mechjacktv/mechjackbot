package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchUsername extends TypedString {

  private TwitchUsername(final String value) {
    super(value);
  }

  public static TwitchUsername of(final String value) {
    return new TwitchUsername(value);
  }

}
