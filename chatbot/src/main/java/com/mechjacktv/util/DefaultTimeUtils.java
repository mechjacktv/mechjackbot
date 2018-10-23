package com.mechjacktv.util;

final class DefaultTimeUtils implements TimeUtils {

  @Override
  public final Integer secondsAsMs(final int seconds) {
    return seconds * SECOND;
  }

}
