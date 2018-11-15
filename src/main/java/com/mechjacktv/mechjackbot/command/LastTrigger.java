package com.mechjacktv.mechjackbot.command;

import com.mechjacktv.util.typedobject.TypedLong;

public final class LastTrigger extends TypedLong {

  private LastTrigger(final Long value) {
    super(value);
  }

  public static LastTrigger of(final Long value) {
    return new LastTrigger(value);
  }

}
