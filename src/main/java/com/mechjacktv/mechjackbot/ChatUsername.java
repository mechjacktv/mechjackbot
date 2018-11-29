package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class ChatUsername extends TypedString {

  public static ChatUsername of(final String value) {
    return TypedString.of(ChatUsername.class, value);
  }

  public ChatUsername(final String value) {
    super(value);
  }

}
