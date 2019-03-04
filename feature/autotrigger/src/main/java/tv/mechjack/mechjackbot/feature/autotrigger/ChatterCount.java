package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.platform.utils.typedobject.TypedInteger;

public final class ChatterCount extends TypedInteger {

  private ChatterCount(final Integer value) {
    super(value);
  }

  public static ChatterCount of(final Integer value) {
    return TypedInteger.of(ChatterCount.class, value);
  }

}
