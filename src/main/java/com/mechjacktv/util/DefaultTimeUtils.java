package com.mechjacktv.util;

import java.util.Objects;

public final class DefaultTimeUtils implements TimeUtils {

  @Override
  public Long currentTime() {
    return System.currentTimeMillis();
  }

  @Override
  public final Integer secondsAsMs(final Integer seconds) {
    Objects.requireNonNull(seconds, "`seconds` **MUST** not be `null`");
    return seconds * SECOND;
  }

  @Override
  public Long hoursAsMs(final Integer hours) {
    Objects.requireNonNull(hours, "`hours` **MUST** not be `null`");
    return hours * HOUR;
  }

}
