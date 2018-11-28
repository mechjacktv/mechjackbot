package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.util.typedobject.TypedString;

public class CommandMessageFormat extends TypedString {

  private CommandMessageFormat(final String value) {
    super(value);
  }

  public static CommandMessageFormat of(final String value) {
    return new CommandMessageFormat(value);
  }

}
