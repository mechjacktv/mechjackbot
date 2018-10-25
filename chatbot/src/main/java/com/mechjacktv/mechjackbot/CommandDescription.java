package com.mechjacktv.mechjackbot;

import com.mechjacktv.typedobject.TypedString;

public final class CommandDescription extends TypedString {

  public static CommandDescription of(final String value) {
    return new CommandDescription(value);
  }

  private CommandDescription(final String value) {
    super(value);
  }

}
