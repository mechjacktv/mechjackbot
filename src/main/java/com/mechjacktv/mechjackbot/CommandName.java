package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandName extends TypedString {

  public static CommandName of(final String value) {
    return TypedString.of(CommandName.class, value);
  }

  public CommandName(final String value) {
    super(value);
  }

}
