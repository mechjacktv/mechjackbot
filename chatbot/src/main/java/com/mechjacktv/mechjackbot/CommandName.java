package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandName extends TypedString {

  public static CommandName of(final String value) {
    return new CommandName(value);
  }

  private CommandName(final String value) {
    super(value);
  }

}
