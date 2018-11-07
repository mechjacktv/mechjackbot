package com.mechjacktv.util;

import java.util.Objects;

final class DefaultTimeUtils implements TimeUtils {

  @Override
  public final Integer secondsAsMs(final Integer seconds) {
    Objects.requireNonNull(seconds, "`seconds` **MUST** not be `null`");
    return seconds * SECOND;
  }

  @Override
  public Long hoursAsMs(final Integer hours) {
    return hours * HOUR;
  }

}
