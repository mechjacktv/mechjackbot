package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchClientId extends TypedString {

  private TwitchClientId(final String value) {
    super(value);
  }

  public static TwitchClientId of(final String value) {
    return new TwitchClientId(value);
  }

}
