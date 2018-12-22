package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class ChatCommandTrigger extends TypedString {

  public static ChatCommandTrigger of(final String value) {
    return TypedString.of(ChatCommandTrigger.class, value);
  }

  private ChatCommandTrigger(final String value) {
    super(value);
  }

}
