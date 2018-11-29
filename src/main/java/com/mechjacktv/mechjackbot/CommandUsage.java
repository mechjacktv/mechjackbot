package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandUsage extends TypedString {

  public static CommandUsage of(final String value) {
    return TypedString.of(CommandUsage.class, value);
  }

  private CommandUsage(final String value) {
    super(value);
  }

}
