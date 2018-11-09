package com.mechjacktv.util;

public interface TimeUtils {

  Integer SECOND = 1000;
  Integer MINUTE = SECOND * 60;
  Long HOUR = MINUTE * 60L;

  Integer secondsAsMs(final Integer seconds);

  Long hoursAsMs(final Integer hours);

}
