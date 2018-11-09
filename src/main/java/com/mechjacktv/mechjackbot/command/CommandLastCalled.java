package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.util.typedobject.TypedLong;

public final class CommandLastCalled extends TypedLong {

  private CommandLastCalled(final Long value) {
    super(value);
  }

  public static CommandLastCalled of(final Long value) {
    return new CommandLastCalled(value);
  }

}
