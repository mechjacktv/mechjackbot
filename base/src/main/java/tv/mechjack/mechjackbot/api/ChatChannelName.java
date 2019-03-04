package tv.mechjack.mechjackbot.api;

import tv.mechjack.platform.utils.typedobject.TypedString;

public final class ChatChannelName extends TypedString {

  public static ChatChannelName of(final String value) {
    return TypedString.of(ChatChannelName.class, value.toLowerCase());
  }

  private ChatChannelName(final String value) {
    super(value);
  }

}
