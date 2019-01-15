package tv.mechjack.mechjackbot.api;

import tv.mechjack.platform.util.typedobject.TypedString;

public final class ChatChannel extends TypedString {

  public static ChatChannel of(final String value) {
    return TypedString.of(ChatChannel.class, value.toLowerCase());
  }

  private ChatChannel(final String value) {
    super(value);
  }

}
