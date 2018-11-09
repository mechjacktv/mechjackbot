package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandTrigger extends TypedString {

  private CommandTrigger(final String value) {
    super(value);
  }

  public static CommandTrigger of(final String value) {
    return new CommandTrigger(value);
  }

}
