package tv.mechjack.mechjackbot.command;

import tv.mechjack.util.typedobject.TypedString;

public final class CommandMessageFormat extends TypedString {

  private CommandMessageFormat(final String value) {
    super(value);
  }

  public static CommandMessageFormat of(final String value) {
    return new CommandMessageFormat(value);
  }

}
