package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.util.typedobject.TypedInteger;

public final class CoolDownPeriodMs extends TypedInteger {

  private CoolDownPeriodMs(final Integer value) {
    super(value);
  }

  public static CoolDownPeriodMs of(final Integer value) {
    return new CoolDownPeriodMs(value);
  }

}
