package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchUserId extends TypedString {

  public static TwitchUserId of(final String value) {
    return new TwitchUserId(value);
  }

  private TwitchUserId(final String value) {
    super(value);
  }

}
