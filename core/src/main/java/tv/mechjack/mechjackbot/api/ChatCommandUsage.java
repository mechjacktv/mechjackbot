package tv.mechjack.mechjackbot.api;

import tv.mechjack.util.typedobject.TypedString;

public final class ChatCommandUsage extends TypedString {

  public static ChatCommandUsage of(final String value) {
    return TypedString.of(ChatCommandUsage.class, value);
  }

  private ChatCommandUsage(final String value) {
    super(value);
  }

}
