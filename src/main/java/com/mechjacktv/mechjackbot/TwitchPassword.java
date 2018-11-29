package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchPassword extends TypedString {

  public static TwitchPassword of(final String value) {
    return TypedString.of(TwitchPassword.class, value);
  }

  private TwitchPassword(final String value) {
    super(value);
  }

}
