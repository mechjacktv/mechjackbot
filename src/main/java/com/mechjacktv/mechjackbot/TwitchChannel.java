package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchChannel extends TypedString {

  public static TwitchChannel of(final String value) {
    return TypedString.of(TwitchChannel.class, value);
  }

  private TwitchChannel(final String value) {
    super(value);
  }

}
