package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class Message extends TypedString {

  public static Message of(final String value) {
    return TypedString.of(Message.class, value);
  }

  private Message(final String value) {
    super(value);
  }

}
