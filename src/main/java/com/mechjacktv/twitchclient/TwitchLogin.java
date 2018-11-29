package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

  public static TwitchLogin of(final String value) {
    return TypedString.of(TwitchLogin.class, value);
  }

  private TwitchLogin(final String value) {
    super(value);
  }

}
