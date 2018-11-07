package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class Message extends TypedString {

  private Message(final String value) {
    super(value);
  }

  public static Message of(final String value) {
    return new Message(value);
  }

}
