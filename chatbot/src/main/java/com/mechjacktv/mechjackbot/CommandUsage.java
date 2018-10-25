package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandUsage extends TypedString {

  public static CommandUsage of(final String value) {
    return new CommandUsage(value);
  }

  private CommandUsage(final String value) {
    super(value);
  }

}
