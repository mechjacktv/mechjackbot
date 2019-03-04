package tv.mechjack.mechjackbot.feature.autotrigger;

import tv.mechjack.platform.utils.typedobject.TypedInteger;

public final class MessageCount extends TypedInteger {

  private MessageCount(final Integer value) {
    super(value);
  }

  public static MessageCount of(final Integer value) {
    return TypedInteger.of(MessageCount.class, value);
  }

}
