package com.mechjacktv.mechjackbot.command.custom;

import com.mechjacktv.util.typedobject.TypedString;

public class CommandBody extends TypedString {

  public static CommandBody of(final String value) {
    return TypedString.of(CommandBody.class, value);
  }

  private CommandBody(final String value) {
    super(value);
  }

}
