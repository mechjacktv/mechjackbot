package com.mechjacktv.twitchclient;

import com.mechjacktv.util.typedobject.TypedString;

public final class TwitchLogin extends TypedString {

  private TwitchLogin(final String value) {
    super(value);
  }

  public static TwitchLogin of(final String value) {
    String sanitizedValue = value.trim().toLowerCase();

    if (sanitizedValue.startsWith("@")) {
      sanitizedValue = sanitizedValue.substring(1);
    }
    return new TwitchLogin(sanitizedValue);
  }

}
