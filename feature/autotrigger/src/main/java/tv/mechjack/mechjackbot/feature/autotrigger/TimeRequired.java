package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.platform.utils.typedobject.TypedInteger;

public final class TimeRequired extends TypedInteger {

  private TimeRequired(final Integer value) {
    super(value);
  }

  public static TimeRequired of(final Integer value) {
    return TypedInteger.of(TimeRequired.class, value);
  }

}
