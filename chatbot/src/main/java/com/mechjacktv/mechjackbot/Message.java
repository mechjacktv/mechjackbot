package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class Message extends TypedString {

  public static Message of(final String value) {
    return new Message(value);
  }

  private Message(final String value) {
    super(value);
  }

}
