package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchUrl extends TypedString {

  public static TwitchUrl of(final String value) {
    return TypedString.of(TwitchUrl.class, value);
  }

  private TwitchUrl(final String value) {
    super(value);
  }

}
