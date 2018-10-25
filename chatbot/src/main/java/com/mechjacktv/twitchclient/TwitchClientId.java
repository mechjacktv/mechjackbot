package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchClientId extends TypedString {

  public static TwitchClientId of(final String value) {
    return new TwitchClientId(value);
  }

  private TwitchClientId(final String value) {
    super(value);
  }

}
