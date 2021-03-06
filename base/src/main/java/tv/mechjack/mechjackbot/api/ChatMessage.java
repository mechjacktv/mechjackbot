package tv.mechjack.mechjackbot.api;

import tv.mechjack.platform.utils.typedobject.TypedString;

public final class ChatMessage extends TypedString {

  public static ChatMessage of(final String value) {
    return TypedString.of(ChatMessage.class, value);
  }

  private ChatMessage(final String value) {
    super(value);
  }

}
