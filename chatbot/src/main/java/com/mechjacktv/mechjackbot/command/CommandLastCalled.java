package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.typedobject.TypedLong;

public final class CommandLastCalled extends TypedLong {

  public static CommandLastCalled of(final Long value) {
    return new CommandLastCalled(value);
  }

  private CommandLastCalled(final Long value) {
    super(value);
  }

}
