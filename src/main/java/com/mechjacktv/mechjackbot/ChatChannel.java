package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class ChatChannel extends TypedString {

  public static ChatChannel of(final String value) {
    return TypedString.of(ChatChannel.class, value.toLowerCase());
  }

  private ChatChannel(final String value) {
    super(value);
  }

}
