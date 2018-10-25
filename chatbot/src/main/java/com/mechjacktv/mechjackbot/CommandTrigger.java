package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandTrigger extends TypedString {

  public static CommandTrigger of(final String value) {
    return new CommandTrigger(value);
  }

  private CommandTrigger(final String value) {
    super(value);
  }

}
