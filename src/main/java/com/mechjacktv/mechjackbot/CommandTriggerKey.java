package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandTriggerKey extends TypedString {

  private CommandTriggerKey(String value) {
    super(value);
  }

  public static CommandTriggerKey of(final String value) {
    return new CommandTriggerKey(value);
  }

}
