package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class ChatCommandDescription extends TypedString {

  public static ChatCommandDescription of(final String value) {
    return TypedString.of(ChatCommandDescription.class, value);
  }

  public ChatCommandDescription(final String value) {
    super(value);
  }

}
