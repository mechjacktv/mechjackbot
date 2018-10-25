package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class TwitchPassword extends TypedString {

  public static TwitchPassword of(final String value) {
    return new TwitchPassword(value);
  }

  private TwitchPassword(final String value) {
    super(value);
  }

}
