package tv.mechjack.mechjackbot.base;

import tv.mechjack.platform.utils.typedobject.TypedInteger;

public final class CoolDownPeriodMs extends TypedInteger {

  public static CoolDownPeriodMs of(final Integer value) {
    return TypedInteger.of(CoolDownPeriodMs.class, value);
  }

  private CoolDownPeriodMs(final Integer value) {
    super(value);
  }

}
