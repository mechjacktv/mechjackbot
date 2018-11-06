package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandName extends TypedString {

  private CommandName(final String value) {
    super(value);
  }

  public static CommandName of(final String value) {
    return new CommandName(value);
  }

}
