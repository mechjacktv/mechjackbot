package com.mechjacktv.twitchclient;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

  public static TwitchLogin of(final String value) {
    return new TwitchLogin(value);
  }

  private TwitchLogin(final String value) {
    super(value);
  }

}
