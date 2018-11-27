package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchChannel extends TypedString {

  private TwitchChannel(final String value) {
    super(value);
  }

  public static TwitchChannel of(final String value) {
    return new TwitchChannel(value.toLowerCase());
  }

}
