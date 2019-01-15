package tv.mechjack.mechjackbot.feature.custom;

import tv.mechjack.platform.util.typedobject.TypedString;

public final class CommandBody extends TypedString {

  private CommandBody(final String value) {
    super(value);
  }

  public static CommandBody of(final String value) {
    return TypedString.of(CommandBody.class, value);
  }

}
