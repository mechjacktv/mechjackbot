package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchUrl extends TypedString {

  public static TwitchUrl of(final String value) {
    return new TwitchUrl(value);
  }

  private TwitchUrl(final String value) {
    super(value);
  }

}
