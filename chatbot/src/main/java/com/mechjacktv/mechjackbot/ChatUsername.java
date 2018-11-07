package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class ChatUsername extends TypedString {

  private ChatUsername(final String value) {
    super(value);
  }

  public static ChatUsername of(final String value) {
    return new ChatUsername(value);
  }

}
