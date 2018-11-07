package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandUsage extends TypedString {

  private CommandUsage(final String value) {
    super(value);
  }

  public static CommandUsage of(final String value) {
    return new CommandUsage(value);
  }

}
