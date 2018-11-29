package com.mechjacktv.mechjackbot.command.core;

import com.mechjacktv.util.typedobject.TypedInteger;

public final class CoolDownPeriodMs extends TypedInteger {

  public static CoolDownPeriodMs of(final Integer value) {
    return TypedInteger.of(CoolDownPeriodMs.class, value);
  }

  private CoolDownPeriodMs(final Integer value) {
    super(value);
  }

}
