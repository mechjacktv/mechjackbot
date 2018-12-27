package tv.mechjack.mechjackbot.command.custom;

import tv.mechjack.util.typedobject.TypedString;

public final class CommandBody extends TypedString {

  public static CommandBody of(final String value) {
    return TypedString.of(CommandBody.class, value);
  }

  private CommandBody(final String value) {
    super(value);
  }

}
