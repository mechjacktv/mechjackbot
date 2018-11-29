package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandTriggerKey extends TypedString {

  public static CommandTriggerKey of(final String value) {
    return TypedString.of(CommandTriggerKey.class, value);
  }

  private CommandTriggerKey(String value) {
    super(value);
  }

}
