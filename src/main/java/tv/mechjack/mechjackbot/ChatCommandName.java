package tv.mechjack.mechjackbot;

import tv.mechjack.util.typedobject.TypedString;

public final class ChatCommandName extends TypedString {

  public static ChatCommandName of(final String value) {
    return TypedString.of(ChatCommandName.class, value);
  }

  public ChatCommandName(final String value) {
    super(value);
  }

}