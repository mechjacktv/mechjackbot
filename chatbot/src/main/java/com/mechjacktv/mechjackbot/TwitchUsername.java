package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchUsername extends TypedString {

  public static TwitchUsername of(final String value) {
    return new TwitchUsername(value);
  }

  private TwitchUsername(final String value) {
    super(value);
  }

}
