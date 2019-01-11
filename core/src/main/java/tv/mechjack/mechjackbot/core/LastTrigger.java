package tv.mechjack.mechjackbot.core;

import tv.mechjack.util.typedobject.TypedLong;

public final class LastTrigger extends TypedLong {

  public static LastTrigger of(final Long value) {
    return TypedLong.of(LastTrigger.class, value);
  }

  private LastTrigger(final Long value) {
    super(value);
  }

}
