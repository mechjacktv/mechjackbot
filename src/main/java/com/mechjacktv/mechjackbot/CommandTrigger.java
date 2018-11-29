package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandTrigger extends TypedString {

  public static CommandTrigger of(final String value) {
    return TypedString.of(CommandTrigger.class, value);
  }

  private CommandTrigger(final String value) {
    super(value);
  }

}
