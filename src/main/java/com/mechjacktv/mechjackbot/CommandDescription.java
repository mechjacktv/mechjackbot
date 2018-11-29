package com.mechjacktv.mechjackbot;

import com.mechjacktv.util.typedobject.TypedString;

public final class CommandDescription extends TypedString {

  public static CommandDescription of(final String value) {
    return TypedString.of(CommandDescription.class, value);
  }

  public CommandDescription(final String value) {
    super(value);
  }

}
