package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.typedobject.TypedInteger;

public final class CommandCoolDownPeriodMs extends TypedInteger {

  public static CommandCoolDownPeriodMs of(final Integer value) {
    return new CommandCoolDownPeriodMs(value);
  }

  private CommandCoolDownPeriodMs(final Integer value) {
    super(value);
  }

}
